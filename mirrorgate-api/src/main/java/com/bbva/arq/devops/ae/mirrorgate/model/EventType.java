package com.bbva.arq.devops.ae.mirrorgate.model;

import java.util.HashMap;
import java.util.Map;

public enum EventType {
    BUILD("BuildType"),
    FEATURE("FeatureType");

    private final String value;

    private EventType(String value){
        this.value = value;
    }

    public String getValue(){
        return value;
    }

    private static final Map<String, EventType> MAPPING = new HashMap<>();

    static{
        MAPPING.put("FeatureType", FEATURE);
        MAPPING.put("BuildType", BUILD);
    }

    public static EventType fromString(String value) {
        if (MAPPING.containsKey(value)){
            return MAPPING.get(value);
        } else {
            throw new IllegalArgumentException();
        }
    }


}
