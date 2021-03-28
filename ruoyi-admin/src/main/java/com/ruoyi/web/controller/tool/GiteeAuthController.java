package com.ruoyi.web.controller.tool;

import com.alibaba.fastjson.JSON;

import com.ruoyi.framework.web.service.SysLoginService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Controller
public class GiteeAuthController {

    @Autowired
    private Configuration configuration;

    @Autowired
    private SysLoginService sysLoginService;

    @GetMapping("/giteeLogin")
    public void giteeLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect("https://gitee.com/oauth/authorize?client_id=e09b8f7795aeb9911bf363992c428f00ec14f0268a49351f95213c84ed4b7a34&redirect_uri=http://localhost:8080/callback&response_type=code&scope=user_info");
    }


    @org.springframework.web.bind.annotation.ResponseBody
    @GetMapping("/callback")
    public void callback(@RequestParam String code, @RequestParam(required = false) String state, HttpServletResponse response) throws IOException, TemplateException {

        String accessToken = getToken(code);

        System.err.println("accessToken: " + accessToken);

        String token = sysLoginService.handleLogin("admin", "admin123");

        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");

        Template template = configuration.getTemplate("hello.ftl");
        template.process(new HashMap<>(){
            {
                put("token", token);
                put("age", 10);
            }
        }, response.getWriter());
        // return token;
    }

    private String getToken(String code) {
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");

        Map<String,Object> map = new HashMap<>() {
            {
                put("grant_type", "authorization_code");
                put("code", code);
                put("client_id", "e09b8f7795aeb9911bf363992c428f00ec14f0268a49351f95213c84ed4b7a34");
                put("redirect_uri", "http://localhost:8080/callback");
                put("client_secret", "0ce26ab4279eb3e4229910ccf80fc46bf0e63067015a34bad81de2ec8fd57000");
            }
        };

        String requestBody = JSON.toJSONString(map);

        Request request = new Request.Builder()
                .url("https://gitee.com/oauth/token")
                .post(RequestBody.create(mediaType, requestBody))
                .build();
        OkHttpClient okHttpClient = new OkHttpClient();

        CompletableFuture<String> completableFuture = new CompletableFuture<>();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.err.println("onFailure: " + e.getMessage());
                completableFuture.completeExceptionally(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.err.println(response.protocol() + " " +response.code() + " " + response.message());
                Headers headers = response.headers();
                for (int i = 0; i < headers.size(); i++) {
                    System.err.println(headers.name(i) + ":" + headers.value(i));
                }
                String body = response.body().string();
                System.err.println("onResponse: " + body);
                completableFuture.complete(body);
            }
        });

        try {
            String res = completableFuture.get();
            return res;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return "";
    }


}
