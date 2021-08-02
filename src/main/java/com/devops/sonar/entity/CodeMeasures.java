package com.devops.sonar.entity;


import lombok.Data;

@Data
public class CodeMeasures {

    private String new_bugs; //新bug
    private String bugs;  //bug
    private String new_coverage; //新覆盖率
    private String coverage; //覆盖率
    private String new_vulnerabilities; //漏洞数
    private String vulnerabilities; //漏洞数
    private String new_duplicated_lines_density; //重复率
    private String duplicated_lines_density; //重复率
    private String alert_status; //状态
    private  String code_smells; //异味


}
