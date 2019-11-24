package com.golfzon.gs.clinet.status.producer.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GsClientStatus {

    @JsonProperty("frame")
    private int frameId;
    private String frameName;

    @JsonProperty("cat")
    private int categoryId;
    private String categoryName;

    @JsonProperty("step")
    private int stepId;
    private String stepName;

    @JsonProperty("acttype")
    private int actionType;
    private String actionName;

    @JsonProperty("lockkey")
    private int lockKey;
    @JsonProperty("shopcode")
    private int shopCode;
    @JsonProperty("systype")
    private int sysType;
    private long time;
    private long online;
    private String ver;
    private String actionGroupKey;
    private long elapsed;
    private String shopName;


    @Builder
    public GsClientStatus(int frameId, int categoryId, int stepId, int actionType, int lockKey, int shopCode, int sysType, long time, long online, String ver) {
        this.frameId = frameId;
        this.categoryId = categoryId;
        this.stepId = stepId;
        this.actionType = actionType;
        this.lockKey = lockKey;
        this.shopCode = shopCode;
        this.sysType = sysType;
        this.time = time;
        this.online = online;
        this.ver = ver;
    }

}
