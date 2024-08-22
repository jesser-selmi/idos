package com.IDOSdigital.userManagement.entities;

import com.IDOSdigital.userManagement.enums.RequestStatus;
import com.IDOSdigital.userManagement.enums.RequestType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;


@Getter
@Setter
@Document
public class Request extends AbstractAuditableEntity {
    private RequestType type;
    private String userId;
    private RequestStatus status = RequestStatus.PENDING;
    private LocalDate date ;
    private int duration ;

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

