package com.devops.sonar.application;

import cn.hutool.json.JSONObject;
import com.devops.sonar.entity.RequestObject;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;


@Component
public class SonarChannelRule extends GeneralChannelRule<RequestObject> {
    @Value("${sonar.url}")
    private  String url;
    @Autowired
    private RestTemplate restTemplate;

    @Override
    public JSONObject process(RequestObject data) {
        JSONObject forObject=null;
        try {
        for (int i = 0; i <3 ; i++) {
            Thread.sleep(3L);
             forObject = restTemplate.getForObject(url + data.getComponent() + "&branch=" + data.getBranch() + "&metricKeys=" + data.getMetricKeys(), JSONObject.class);
            if (forObject.isEmpty()|| forObject==null || forObject.size()==0) {
                    Thread.sleep(3L);
            }else{
                break;
            }

            if (i==2){
                throw  new RuntimeException("获取检测代码数据异常");
            }
        }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return forObject;
    }

}
