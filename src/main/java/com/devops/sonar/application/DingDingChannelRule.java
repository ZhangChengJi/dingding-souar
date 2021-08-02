package com.devops.sonar.application;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.devops.sonar.entity.CodeMeasures;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class DingDingChannelRule extends GeneralChannelRule<String> {

    @Value("${dingding.url}")
    private  String url;
    @Autowired
    private RestTemplate restTemplate;
    @Override
    public JSONObject process(String result) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        JSONObject result1 = new JSONObject();
        JSONObject text = new JSONObject();
        text.put("content", result);
        result1.put("text", text);
        result1.put("msgtype", "text");
        String jsonString = JSONUtil.toJsonPrettyStr(result1);
        HttpEntity<String> request = new HttpEntity<>(jsonString, headers);
        Object o = restTemplate.postForEntity(url, request,String.class );
        System.out.println(o);
        return null;
    }
}
