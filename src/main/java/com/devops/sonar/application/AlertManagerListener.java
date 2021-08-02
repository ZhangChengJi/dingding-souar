package com.devops.sonar.application;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.devops.sonar.util.R;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiRobotSendRequest;
import com.dingtalk.api.response.OapiRobotSendResponse;
import com.taobao.api.ApiException;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.function.BiConsumer;

@RestController
public class AlertManagerListener {

    @PostMapping("/send")
    @ResponseBody
    public R alertManagerSend(@RequestBody String json) throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException {
        if (StrUtil.isBlank(json)) {
            return R.failed("报警失败");
        }
        JSONObject object = JSONUtil.parseObj(json);
        String status = object.getStr("status");
        String alertname = object.getJSONObject("groupLabels").get("alertname").toString();
        String externalURL = object.getStr("externalURL")+"/#/alerts?receiver="+object.get("webhook");
        JSONArray alerts = object.getJSONArray("alerts");
        JSONObject AlertJSON=new JSONObject();
        for (int i = 0; i <alerts.size() ; i++) {
            AlertJSON= alerts.getJSONObject(i);
        }
        JSONObject annotations = AlertJSON.getJSONObject("annotations");
        String summary = annotations.getStr("summary");
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String startsAt = AlertJSON.getStr("startsAt").substring(0,AlertJSON.getStr("startsAt").indexOf("."));

        LocalDateTime parse = LocalDateTime.parse(startsAt);
        String format1 = format.format(parse);

        String description = annotations.getStr("description");
        String value = "<font size=3 color=#FF0000>"+annotations.getStr("value")+"</font>"+(annotations.getStr("unit")!=null ? annotations.getStr("unit"):"");
        String note = annotations.getStr("note")!=null ? annotations.getStr("note"):"";
        Long timestamp = System.currentTimeMillis();
        String secret = "SEC8870244f465939f54b429a4560525acbf7d184807b13d3bdf6038b0d25f7310a";
        String stringToSign = timestamp + "\n" + secret;
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(secret.getBytes("UTF-8"), "HmacSHA256"));
        byte[] signData = mac.doFinal(stringToSign.getBytes("UTF-8"));
        String sign = URLEncoder.encode(new String(Base64.encodeBase64(signData)),"UTF-8");
        String sign1 = "&timestamp=" + timestamp + "&sign=" + sign;
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/robot/send?access_token=311f83b9c37435f0002cbb7489631235667be8229c2d31579443cda61ac78ffc"+sign1);

        OapiRobotSendRequest request = new OapiRobotSendRequest();

        request.setMsgtype("markdown");
        OapiRobotSendRequest.Markdown markdown = new OapiRobotSendRequest.Markdown();
        markdown.setTitle("集群告警");
        markdown.setText("## ["+alertname+"]("+externalURL+")\n" +
                "\n" +
                "\uD83D\uDD52触发时间："+format1+"\n" +
                "\n" +
                "☄️事件："+summary+"\n" +
                "\n" +
                "🔥描述: "+description+"  "+value+note+"\n" +
                "\n");
        request.setMarkdown(markdown);
        try {
            OapiRobotSendResponse rsp = client.execute(request);

        } catch (ApiException e) {
            e.printStackTrace();
        }
     return R.ok();
    }
}
