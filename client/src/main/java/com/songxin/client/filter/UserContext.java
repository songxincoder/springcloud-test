package com.songxin.client.filter;

public class UserContext {

    public static final String CORRELATION_ID="CORRELATION_ID";
    public static final String USER_ID="USER_ID";
    public static final String AUTH_TOKEN="AUTH_TOKEN";
    public static final String ORG_ID="ORG_ID";


    private String correlationId;
    private String userId;

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
