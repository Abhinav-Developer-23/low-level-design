package org.example.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Objects;

@Getter

public class Tag {
    private final String tagId;
    private final String name;
    private final String description;

    public Tag(String tagId, String name, String description) {
        this.tagId = tagId;
        this.name = name.toLowerCase(); // Normalize tag names
        this.description = description;
    }

}

