package com.bbva.arq.devops.ae.mirrorgate.dto;

import java.util.List;

public class NotificationDTO {

    private List<String> dashboardIds;
    private String message;

    public List<String> getDashboardIds() {
        return dashboardIds;
    }

    public void setDashboardIds(List<String> dashboardIds) {
        this.dashboardIds = dashboardIds;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
