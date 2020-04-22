package com.example.storagedemo;

public class Upload {
    String name, url;

    public Upload() {

    }

    public Upload(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
}
