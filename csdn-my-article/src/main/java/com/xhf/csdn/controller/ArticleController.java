package com.xhf.csdn.controller;

import com.xhf.csdn.model.ArticleRequest;
import com.xhf.csdn.model.Result;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/articles")
public class ArticleController {
    
    @PostMapping
    public Result<String> getArticleContent(@RequestBody ArticleRequest request) {
        // 示例返回数据，后续可替换为真实数据逻辑
        return Result.success("{\"content\": \"示例文章内容\"}");
    }
}