package com.backendstarter.threadboard.model;

import java.util.Objects;

public class PostPostRequestBody {

    private String body;

    public PostPostRequestBody(String body) {
        this.body = body;
    }

    public PostPostRequestBody() {
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PostPostRequestBody that = (PostPostRequestBody) o;
        return Objects.equals(body, that.body);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(body);
    }
}
