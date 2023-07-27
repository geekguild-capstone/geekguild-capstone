package com.geekguild.models;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter

public class ProfileFormWrapper {
    private User user;
    private Portfolio portfolio;
    private Work work;
    private List<Language> allLanguages; // New property to hold all available languages
    private List<Long> selectedLanguages = new ArrayList<>(); // Initialize with an empty list


}
