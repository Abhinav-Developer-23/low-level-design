package org.example.model;

import java.util.Objects;

public class Answer extends Post {
    private final Question question;
    private boolean isAccepted;

    public Answer(String answerId, User author, String content, Question question) {
        super(answerId, author, content);
        this.question = question;
        this.isAccepted = false;
    }

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

    public Question getQuestion() {
        return question;
    }

    public boolean isAccepted() {
        return isAccepted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Answer answer = (Answer) o;
        return Objects.equals(id, answer.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Answer{" +
                "author=" + author.getUsername() +
                ", votes=" + getVoteCount() +
                ", accepted=" + isAccepted +
                '}';
    }
}
