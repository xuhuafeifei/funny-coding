package com.xhf.csdn.service;

import com.xhf.csdn.model.Article;
import com.xhf.csdn.model.ArticleRequest;
import com.xhf.csdn.model.Result;
import com.xhf.csdn.utils.GsonUtils;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.xhf.csdn.utils.HtmlToMarkdownUtils;
import com.xhf.csdn.utils.HttpClient;
import org.apache.http.impl.cookie.BasicClientCookie2;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.xhf.csdn.model.HttpRequest;
import com.xhf.csdn.model.HttpResponse;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletResponse; // 添加缺少的导入语句
import java.io.OutputStream;
import java.io.File; // 添加缺少的导入语句
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path; // 添加缺少的导入语句
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipOutputStream; // 添加缺少的导入语句
import java.io.IOException; // 添加缺少的导入语句
import java.util.zip.ZipEntry;
import java.nio.file.Files;
import java.io.InputStream;
import java.net.URL;
import java.util.regex.Pattern;
import java.util.regex.Matcher; // 由于使用了 Matcher，也需要导入该类

@Service
public class ArticleService {

    private String basicUrl = "https://blog.csdn.net/qq_62835094/article/details/%s";

    public Article getArticleContent(ArticleRequest request) {
        String url = String.format(basicUrl, request.getArticleId());

        Article article = new Article();

        Document document;
        try {
            document = Jsoup.connect(url).get();
        } catch (IOException e) {
            throw new RuntimeException("Failed to fetch the document from the URL: " + url, e);
        }
        Element blogContentBox = document.getElementsByClass("blog-content-box").get(0);
        // 获取文章标题
        Element title = blogContentBox.getElementById("articleContentId");
        // 获取title的内部值
        String titleValue = title.text();
        article.setTitle(titleValue);

        Element contentViews = blogContentBox.getElementById("content_views");
        String htmlContent = contentViews.toString();

        article.setContent(htmlContent);
        article.setMarkdowncontent(HtmlToMarkdownUtils.convert(htmlContent));

        return article;
    }

    // Bug修复：将错误的类型名HttpServerletResponse修改为正确的HttpServletResponse
    public void downloadArticle(ArticleRequest request, javax.servlet.http.HttpServletResponse response) {
        Article article = this.getArticleContent(request);
        if (article == null) {
            return;
        }
        int downloadType = request.getDownloadType();
        if (downloadType == 0) {
            System.out.println("默认下载");
            defaultDownload(article, response);
        } else if (downloadType == 1) {
            System.out.println("图片链接下载");
            imageLinkDownload(article, response);
        } else if (downloadType == 2) {
            System.out.println("去除水印下载");
            removeWatermarkDownload(article, response);
        }
    }


    /**
     * 默认下载方法
     *
     * @param response       HttpServletResponse 对象
     */
    private void defaultDownload(Article article, HttpServletResponse response) {
        try {
            // 动态生成文件名
            String fileName = article.getTitle() + ".md";

            // 设置响应头
            response.setContentType("application/octet-stream");

            // 对文件名进行 URL 编码
            String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString())
                    .replaceAll("\\+", "%20"); // 替换空格为 %20

            // 设置 Content-Disposition 头部，兼容多种浏览器
            String contentDisposition = "attachment; filename=\"" + encodedFileName + "\"; filename*=UTF-8''" + encodedFileName;
            response.setHeader("Content-Disposition", contentDisposition);

            // 将文章内容写入响应流
            byte[] fileBytes = article.getMarkdowncontent().getBytes(StandardCharsets.UTF_8); // 确保使用 UTF-8 编码
            OutputStream outputStream = response.getOutputStream();
            outputStream.write(fileBytes);
            outputStream.flush();
        } catch (Exception e) {
            throw new RuntimeException("默认下载失败！", e);
        }
    }


     /**
     * 处理图片链接下载（生成 ZIP 文件）
     *
     * @param article 文章对象
     * @param response HTTP 响应
     */
     private void imageLinkDownload(Article article, HttpServletResponse response) {
         // 临时目录路径（用于存储下载的图片）
         Path tempDir = null;
         try {
             // 创建临时目录
             tempDir = Files.createTempDirectory("article-download-");
             String markdownContent = article.getMarkdowncontent();

             // 解析引用的图片映射表
             Map<String, String> imageReferences = parseImageReferences(markdownContent);

             // 正则表达式匹配图片链接
             Pattern pattern = Pattern.compile("!\\[[^\\]]*\\](?:\\(([^\\)]+)\\)|\\[([^\\]]+)\\])");
             Matcher matcher = pattern.matcher(markdownContent);

             // 替换后的 Markdown 内容
             StringBuffer modifiedMarkdown = new StringBuffer();
             Path imagesDir = tempDir.resolve("images");
             Files.createDirectories(imagesDir); // 创建 images 目录

             while (matcher.find()) {
                 String standardUrl = matcher.group(1); // 标准语法中的 URL
                 String referenceKey = matcher.group(2); // 带引用语法中的引用标识符

                 String imageUrl = standardUrl != null ? standardUrl : imageReferences.get(referenceKey);
                 if (imageUrl != null) {
                     String imageFileName = downloadImage(imageUrl, imagesDir);
                     String replacement = "![](images/" + imageFileName + ")";
                     matcher.appendReplacement(modifiedMarkdown, replacement);
                 }
             }
             matcher.appendTail(modifiedMarkdown);

             // 生成 ZIP 文件名
             String zipFileName = article.getTitle() + ".zip";

             // 对文件名进行 URL 编码，解决中文乱码问题
             String encodedZipFileName = URLEncoder.encode(zipFileName, StandardCharsets.UTF_8.toString())
                     .replaceAll("\\+", "%20"); // 替换空格为 %20

             // 设置响应头，兼容多种浏览器
             String contentDisposition = "attachment; filename=\"" + encodedZipFileName + "\"; filename*=UTF-8''" + encodedZipFileName;
             response.setContentType("application/zip");
             response.setHeader("Content-Disposition", contentDisposition);

             // 将 ZIP 文件写入响应流
             try (ZipOutputStream zipOut = new ZipOutputStream(response.getOutputStream())) {
                 // 添加修改后的 Markdown 文件到 ZIP
                 zipOut.putNextEntry(new ZipEntry(article.getTitle() + ".md"));
                 zipOut.write(modifiedMarkdown.toString().getBytes(StandardCharsets.UTF_8));
                 zipOut.closeEntry();

                 // 添加 images 目录到 ZIP
                 addDirectoryToZip(imagesDir, "images", zipOut);
             }

         } catch (Exception e) {
             throw new RuntimeException("图片链接下载失败！", e);
         } finally {
             // 清理临时目录
             if (tempDir != null) {
                 deleteDirectory(tempDir.toFile());
             }
         }
     }

    /**
     * 解析 Markdown 中的图片引用映射表
     *
     * @param markdownContent Markdown 内容
     * @return 引用标识符到实际 URL 的映射表
     */
    private Map<String, String> parseImageReferences(String markdownContent) {
        Map<String, String> references = new HashMap<>();
        Pattern pattern = Pattern.compile("^\\s*\\[([^\\]]+)\\]:\\s*(.+)$", Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(markdownContent);

        while (matcher.find()) {
            String referenceKey = matcher.group(1).trim(); // 获取引用标识符
            String referenceUrl = matcher.group(2).trim(); // 获取实际 URL
            references.put(referenceKey, referenceUrl);
        }
        return references;
    }

    /**
     * 下载图片到指定目录
     *
     * @param imageUrl  图片 URL
     * @param imagesDir 图片存储目录
     * @return 保存的图片文件名
     */
    private String downloadImage(String imageUrl, Path imagesDir) throws IOException {
        String fileName = "image-" + System.currentTimeMillis() + ".png"; // 生成唯一文件名
        Path imagePath = imagesDir.resolve(fileName);

        try (InputStream in = new URL(imageUrl).openStream()) {
            Files.copy(in, imagePath);
        }
        return fileName;
    }

    /**
     * 递归添加目录到 ZIP 文件
     *
     * @param dirPath      目录路径
     * @param entryPrefix  ZIP 条目前缀
     * @param zipOut       ZIP 输出流
     */
    private void addDirectoryToZip(Path dirPath, String entryPrefix, ZipOutputStream zipOut) throws IOException {
        Files.walk(dirPath)
                .filter(Files::isRegularFile)
                .forEach(file -> {
                    try {
                        Path relativePath = dirPath.relativize(file);
                        ZipEntry entry = new ZipEntry(entryPrefix + "/" + relativePath.toString().replace("\\", "/"));
                        zipOut.putNextEntry(entry);
                        Files.copy(file, zipOut);
                        zipOut.closeEntry();
                    } catch (IOException e) {
                        throw new RuntimeException("添加文件到 ZIP 失败！", e);
                    }
                });
    }

    /**
     * 删除临时目录及其内容
     *
     * @param directory 临时目录
     */
    private void deleteDirectory(File directory) {
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    deleteDirectory(file);
                }
            }
        }
        directory.delete();
    }

    private void removeWatermarkDownload(Article article, HttpServletResponse response) {

    }
}