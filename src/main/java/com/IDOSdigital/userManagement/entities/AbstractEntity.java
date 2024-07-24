package com.IDOSdigital.userManagement.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Getter
@Setter
public abstract class AbstractEntity {
    @Id
    protected String id;

    private boolean deleted = false;

    public String getId() {
        return id;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }


}
