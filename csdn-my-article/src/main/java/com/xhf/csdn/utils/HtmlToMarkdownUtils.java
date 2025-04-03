package com.xhf.csdn.utils;

import com.overzealous.remark.Remark;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.ext.toc.TocExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Document;
import com.vladsch.flexmark.util.data.MutableDataSet;

import java.util.Arrays;

/**
 * HTML 转 Markdown 工具类
 */
public class HtmlToMarkdownUtils {
    public static String convert(String html) {
        Remark remark = new Remark();
        String content = remark.convert(html);
        return content;
    }
}