package com.bbva.arq.devops.ae.mirrorgate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

/**
 * Base model with Base Object Id.
 */
public abstract class BaseIdModel implements BaseModel {

    @Id
    @JsonIgnore
    private ObjectId id;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }
}
