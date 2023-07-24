package com.geekguild.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class ProfileFormWrapper {
    private User user;
    private Portfolio portfolio;
    private Work work;
}
