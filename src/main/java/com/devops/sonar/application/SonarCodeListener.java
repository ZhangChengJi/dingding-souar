package com.devops.sonar.application;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.devops.sonar.constant.ChannelRuleEnum;
import com.devops.sonar.constant.CommonConstants;
import com.devops.sonar.entity.GitlabCIDto;
import com.devops.sonar.entity.RequestObject;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiRobotSendRequest;
import com.dingtalk.api.request.OapiUserGetRequest;
import com.dingtalk.api.response.OapiRobotSendResponse;
import com.dingtalk.api.response.OapiUserGetResponse;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.taobao.api.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

import static javafx.scene.input.KeyCode.R;

@RestController
public class SonarCodeListener {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${sonar.url}")
    private  String url;
    @Autowired
    private SonarChannelRule sonarChannelRule;
    @Autowired
    private DingDingChannelRule dingDingChannelRule;
    @PostMapping("/working")
    public void working(@RequestBody GitlabCIDto gitlabCIDto){
        RequestObject requestObject = new RequestObject();
        requestObject.setComponent(gitlabCIDto.getCiProjectName());
        requestObject.setBranch(gitlabCIDto.getCiCommitRefName());
        requestObject.setMetricKeys("bugs,coverage,duplicated_lines_density,code_smells,alert_status,vulnerabilities");
        JSONObject json = sonarChannelRule.process(requestObject);
        Object measures = json.get("component");
        JSONObject parse = JSONUtil.parseObj(measures);
        List<Map<String,String>> measures1 = (List<Map<String,String>>) parse.getObj("measures");
        String bug="";
        String dlu="";
        String vu="";
        String cs="";
        String co="";
        String as="";
        for (int i = 0; i < measures1.size(); i++) {
            ChannelRuleEnum match = ChannelRuleEnum.match(measures1.get(i).get(CommonConstants.METRIC).toString(), measures1.get(i).get(CommonConstants.VALUE).toString());
            switch (match.key) {
                case "bugs":
                    bug = match.name + match.val;
                    break;
                case "duplicated_lines_density":
                    dlu = match.name + match.val;
                    break;
                case "coverage":
                    co = match.name + match.val;
                    break;
                case "code_smells":
                    cs = match.name + match.val;
                    break;
                case "vulnerabilities":
                    vu = match.name + match.val;
                    break;
                case "alert_status":
                    if ("ERROR".equals(match.val)) {
                        as = "### "+match.name + "<font size=5 color=#FF0000>"+match.val+"</font>";
                    }else{
                        as = "### "+match.name + "<font size=5 color=#00DD77>"+match.val+"</font>";
                    }

                    break;
                default:
                    throw new RuntimeException("状态不正确");

            }

        }
        String gitlaburl="http://192.168.1.162/"+gitlabCIDto.getCiProjectPath()+"/-/commit/"+gitlabCIDto.getCiCommitSha();
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/robot/send?access_token=f685dd3bc974eca5f2426b419a4c12cf4e4635e14c78bad644c9850e16462167");
        OapiRobotSendRequest request = new OapiRobotSendRequest();
        request.setMsgtype("markdown");
        OapiRobotSendRequest.Markdown markdown = new OapiRobotSendRequest.Markdown();
        markdown.setTitle("代码检测通知");
        markdown.setText("### "+gitlabCIDto.getCiProjectName()+" ["+gitlabCIDto.getCiCommitTitle()+"]("+gitlaburl+")\n" +
                "\n" +
                "![团队合作委员会.png](https://i.loli.net/2020/09/28/eb8mOZjxXPJdM4G.png)\n" +
                "\n" +
                as+"\n"+
                ">\uD83D\uDC1E"+bug+"\n" +
                ">\n" +
                ">\uD83C\uDF0B"+vu+"\n" +
                ">\n" +
                ">\uD83E\uDD91"+cs+"\n" +
                ">\n" +
                ">\uD83D\uDD25"+co+"\n" +
                ">\n" +
                ">\uD83D\uDE40"+dlu+"\n" +
                ">\n" +
                ">###### 前往Sonarqube [查看](http://192.168.1.244:30003/dashboard?id="+gitlabCIDto.getCiProjectName()+"&&branch="+gitlabCIDto.getCiCommitRefName()+")\n"
        );
        request.setMarkdown(markdown);
        try {
            OapiRobotSendResponse rsp = client.execute(request);
        } catch (ApiException e) {
            e.printStackTrace();
        }


    }
}