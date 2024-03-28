package com.ll.edubridge.standard.base;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum KwTypeCourse {
    ALL("all"),
    TITLE("title"),
    HASHTAGS("hashtags"),
    NAME("owner");

    private final String value;
}
