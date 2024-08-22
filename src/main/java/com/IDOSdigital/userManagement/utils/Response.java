package com.IDOSdigital.userManagement.utils;

public class Response {
    private Object data;
    private EntityResponse entityResponse;
    public Object getData() {
        return data;
    }
    public void setData(Object data) {
        this.data = data;
    }
    public EntityResponse getEntityResponse() {
        return entityResponse;
    }
    public void setEntityResponse(EntityResponse entityResponse) {
        this.entityResponse = entityResponse;
    }
}
