package com.geekguild.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentUpdateRequest {

    private String text;
    private String snippet;
}
