package org.example.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Objects;

@EqualsAndHashCode(callSuper = false)
@Getter
public class Answer extends Post {
    private final Question question;
    private boolean isAccepted;

    public Answer(String answerId, User author, String content, Question question) {
        super(answerId, author, content);
        this.question = question;
        this.isAccepted = false;
    }


    //#imp
    public synchronized void markAsAccepted() {
        this.isAccepted = true;
    }

    public synchronized void unmarkAsAccepted() {
        this.isAccepted = false;
    }

    // Getters
    public String getAnswerId() {
        return id;
    }

    public boolean isAccepted() {
        return isAccepted;
    }


}
