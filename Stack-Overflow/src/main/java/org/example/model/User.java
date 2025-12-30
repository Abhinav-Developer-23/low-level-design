package org.example.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.example.enums.UserRole;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
@Getter
@Setter
@EqualsAndHashCode
public class User {

    private final String userId;

    private final String username;

    private final String email;

    private UserRole role;

    private int reputation;

    private final LocalDateTime createdAt;
    private final List<Question> questions;
    private final List<Answer> answers;
    private final List<Comment> comments;

    public User(String userId, String username, String email, UserRole role) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.role = role;
        this.reputation = 0;
        this.createdAt = LocalDateTime.now();
        // Using thread-safe collections for concurrent access
        this.questions = new CopyOnWriteArrayList<>();
        this.answers = new CopyOnWriteArrayList<>();
        this.comments = new CopyOnWriteArrayList<>();
    }

    // Synchronized method for thread-safe reputation updates
    public synchronized void addReputation(int points) {
        this.reputation += points;
    }

    public void addQuestion(Question question) {
        this.questions.add(question);
    }

    public void addAnswer(Answer answer) {
        this.answers.add(answer);
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
    }

    public List<Question> getQuestions() {
        return new ArrayList<>(questions);
    }

    public List<Answer> getAnswers() {
        return new ArrayList<>(answers);
    }

    public List<Comment> getComments() {
        return new ArrayList<>(comments);
    }


}

