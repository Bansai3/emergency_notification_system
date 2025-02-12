package com.vfedotov.notification.dao.entity;

public enum Status {
    IN_PROCESS("in_process"), SEND("send");

    private final String status;

    Status(String status) {
        this.status = status;
    }

    String getStatus() {
        return status;
    }

}
