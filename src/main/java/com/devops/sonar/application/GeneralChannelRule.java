package com.devops.sonar.application;

import cn.hutool.json.JSONObject;
import com.fasterxml.jackson.databind.util.JSONPObject;

public abstract class GeneralChannelRule <T> {
public abstract JSONObject process(T data);

}
