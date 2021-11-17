package com.effective.common.frequency.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class App extends Operator {

    private Integer id;

    private String appName;

    private Boolean osSwitch;

    private String osVersion;

    private String appSwitch;

    private String appVersion;

    private String pushProtocolVersion;

    private String subscribeSwitch;

    private Integer frequencyId;

    private Integer keywordId;

    private Integer blackListId;

    private Integer whiteListId;

    private Integer yn;

}
