package com.devops.sonar.constant;

import lombok.Data;
import lombok.Setter;


public enum ChannelRuleEnum {
    BUGS("bugs","bug数:",null),
    DUPLICATED_LINES_DENSITY("duplicated_lines_density","重复率:",null),
    COVERAGE("coverage","覆盖率:",null),
    CODE_SMELLS("code_smells","异味:",null),
    VULNERABILITIES("vulnerabilities","漏洞:",null),
    ALERT_STATUS("alert_status","状态:",null);




   public String key;
    public String name;
    @Setter
    public String val;

    ChannelRuleEnum(String key, String name,String val) {
        this.key=key;
        this.name=name;
        this.val=val;
    }

    public static ChannelRuleEnum match(String key,String val){
        ChannelRuleEnum[] values = ChannelRuleEnum.values();
        for (ChannelRuleEnum value : values) {
            if (value.key.equals(key)) {
                value.setVal(val);
                return value;
            }
        }
        return null;
    }

}
