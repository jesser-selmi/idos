package com.IDOSdigital.userManagement.entities;

import com.IDOSdigital.userManagement.enums.RequestType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;


@Getter
@Setter
@Document
public class Request extends AbstractAuditableEntity {
    private RequestType type;
    private String userId;

    public RequestType getType() {
        return type;
    }

    public void setType(RequestType type) {
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}

