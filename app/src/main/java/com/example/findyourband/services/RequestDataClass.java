package com.example.findyourband.services;

import java.util.Objects;

public class RequestDataClass {
    private String id;
    private String from;
    private String to;

    private String status; // send, accept, deny
    private boolean type; // 0 - from band to musician, 1 - from musician to band

    public RequestDataClass() {
    }


    public RequestDataClass(String id, String from, String to, String status, boolean type) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.status = status;
        this.type = type;
    }

    public RequestDataClass(String from, String to, boolean type) {
        this.from = from;
        this.to = to;
        this.type = type;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public boolean isType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RequestDataClass that = (RequestDataClass) o;
        return from.equals(that.from) && to.equals(that.to) && status.equals(that.status) && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to, status, type);
    }
}
