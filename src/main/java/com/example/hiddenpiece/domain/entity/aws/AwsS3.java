package com.example.hiddenpiece.domain.entity.aws;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AwsS3 {
    private String key;
    private String path;

    @Builder
    public AwsS3(String key, String path) {
        this.key = key;
        this.path = path;
    }
}