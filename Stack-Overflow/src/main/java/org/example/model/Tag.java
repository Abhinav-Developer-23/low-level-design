package org.example.model;

import java.util.Objects;

public class Tag {
    private final String tagId;
    private final String name;
    private final String description;

    public Tag(String tagId, String name, String description) {
        this.tagId = tagId;
        this.name = name.toLowerCase(); // Normalize tag names
        this.description = description;
    }

    public String getTagId() {
        return tagId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return Objects.equals(tagId, tag.tagId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tagId);
    }

    @Override
    public String toString() {
        return name;
    }
}

