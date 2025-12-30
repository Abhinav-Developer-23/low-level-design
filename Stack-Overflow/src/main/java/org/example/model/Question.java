package org.example.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.example.enums.QuestionStatus;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
@Getter
@EqualsAndHashCode(callSuper = false)
public class Question extends Post {
    private String title;

    private QuestionStatus status;
    private final AtomicInteger viewCount;
    private final Set<Tag> tags;
    private final List<Answer> answers;

    public Question(String questionId, User author, String title, String content, Set<Tag> tags) {
        super(questionId, author, content);
        this.title = title;
        this.status = QuestionStatus.OPEN;
        this.viewCount = new AtomicInteger(0);
        this.tags = new HashSet<>(tags);
        this.answers = new CopyOnWriteArrayList<>();
    }

    public synchronized void updateQuestion(String newTitle, String newContent) {
        this.title = newTitle;
        updateContent(newContent);
    }

    public synchronized void updateStatus(QuestionStatus newStatus) {
        this.status = newStatus;
    }

    public void incrementViewCount() {
        viewCount.incrementAndGet();
    }

    public void addAnswer(Answer answer) {
        answers.add(answer);
    }

    public void addTag(Tag tag) {
        tags.add(tag);
    }

    public Optional<Answer> getAcceptedAnswer() {
        return answers.stream()
                .filter(Answer::isAccepted)
                .findFirst();
    }

    // Getters
    public String getQuestionId() {
        return id;
    }

    public int getViewCount() {
        return viewCount.get();
    }

    public Set<Tag> getTags() {
        return new HashSet<>(tags);
    }

    public List<Answer> getAnswers() {
        return new ArrayList<>(answers);
    }


}
