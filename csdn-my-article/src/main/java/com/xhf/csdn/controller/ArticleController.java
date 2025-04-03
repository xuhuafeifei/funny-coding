package com.xhf.csdn.controller;

import com.xhf.csdn.model.Article;
import com.xhf.csdn.model.ArticleRequest;
import com.xhf.csdn.model.Result;
import com.xhf.csdn.service.ArticleService;
import com.google.gson.JsonParser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse; // 添加缺少的导入语句

@RestController
@RequestMapping("/api")
@CrossOrigin
public class ArticleController {

    private JsonParser jsonParser = new com.google.gson.JsonParser();

    @Autowired
    private ArticleService articleService;

    /**
     * 获取文章信息
     * @param request
     * @return
     */
    @PostMapping("/getArticleContent")
    @CrossOrigin
    public Result<String> getArticleContent(@RequestBody ArticleRequest request) {
        Article article = articleService.getArticleContent(request);
        if (article == null) {
            return Result.error(500, "获取文章内容失败");
        }
        String articleContent = article.getMarkdowncontent();

        return Result.success(articleContent);
    }

    /**
     * 下载文章
     */
    @PostMapping("/downloadArticle")
    @CrossOrigin
    public Result<String> downloadArticle(@RequestBody ArticleRequest request, HttpServletResponse response) {
        // 实现文章下载逻辑
        articleService.downloadArticle(request, response);

        return Result.success("下载成功");
    }

    /**
     * 下载文章
     */
    @GetMapping("/downloadArticleSimple")
    @CrossOrigin
    public Result<String> downloadArticleSimple(@RequestParam("articleId") String articleId,
                                                @RequestParam("downloadType") int downloadType,
                                                HttpServletResponse response) {
        // 实现文章下载逻辑
        ArticleRequest article = new ArticleRequest();
        article.setArticleId(articleId);
        article.setDownloadType(downloadType);
        articleService.downloadArticle(article, response);

        return Result.success("下载成功");
    }

    /**
     * 批量爬取文章
     */
}