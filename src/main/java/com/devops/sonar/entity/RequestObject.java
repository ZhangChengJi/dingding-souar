package com.devops.sonar.entity;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

@Data
public class RequestObject {
    private String component;
    private String branch;

    private String metricKeys;
}
