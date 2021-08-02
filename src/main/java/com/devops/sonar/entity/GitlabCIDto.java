package com.devops.sonar.entity;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class GitlabCIDto implements Serializable {

    private String ciProjectName;
    private String ciCommitRefName;
    private String ciCommitTitle;
    private  String ciCommitSha;
    private  String ciProjectPath;
}
