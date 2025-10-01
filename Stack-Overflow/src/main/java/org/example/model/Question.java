package org.example.model;

import org.example.enums.QuestionStatus;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class Question {
    private final String questionId;
    private final User author;
    private String title;
    private String content;
    private QuestionStatus status;
    private final AtomicInteger voteCount;
    private final AtomicInteger viewCount;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private final Set<Tag> tags;
    private final Map<String, Vote> votes; // userId -> Vote
    private final List<Answer> answers;
    private final List<Comment> comments;

    public Question(String questionId, User author, String title, String content, Set<Tag> tags) {
        this.questionId = questionId;
        this.author = author;
        this.title = title;
        this.content = content;
        this.status = QuestionStatus.OPEN;
        this.voteCount = new AtomicInteger(0);
        this.viewCount = new AtomicInteger(0);
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
        this.tags = new HashSet<>(tags);
        this.votes = new ConcurrentHashMap<>();
        this.answers = new CopyOnWriteArrayList<>();
        this.comments = new CopyOnWriteArrayList<>();
    }

    public synchronized void updateContent(String newTitle, String newContent) {
        this.title = newTitle;
        this.content = newContent;
        this.updatedAt = LocalDateTime.now();
    }

    public synchronized void updateStatus(QuestionStatus newStatus) {
        this.status = newStatus;
    }

    public void incrementViewCount() {
        viewCount.incrementAndGet();
    }

    public Vote addVote(Vote vote) {
        Vote existingVote = votes.get(vote.getUser().getUserId());
        
        if (existingVote != null) {
            // User already voted, calculate the difference
            int oldValue = existingVote.getVoteType().getValue();
            int newValue = vote.getVoteType().getValue();
            
            if (oldValue != newValue) {
                existingVote.changeVote(vote.getVoteType());
                voteCount.addAndGet(newValue - oldValue);
            }
            return existingVote;
        } else {
            votes.put(vote.getUser().getUserId(), vote);
            voteCount.addAndGet(vote.getVoteType().getValue());
            return vote;
        }
    }

    public boolean removeVote(String userId) {
        Vote vote = votes.remove(userId);
        if (vote != null) {
            voteCount.addAndGet(-vote.getVoteType().getValue());
            return true;
        }
        return false;
    }

    public void addAnswer(Answer answer) {
        answers.add(answer);
    }

    public void addComment(Comment comment) {
        comments.add(comment);
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
        return questionId;
    }

    public User getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public QuestionStatus getStatus() {
        return status;
    }

    public int getVoteCount() {
        return voteCount.get();
    }

    public int getViewCount() {
        return viewCount.get();
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public Set<Tag> getTags() {
        return new HashSet<>(tags);
    }

    public List<Answer> getAnswers() {
        return new ArrayList<>(answers);
    }

    public List<Comment> getComments() {
        return new ArrayList<>(comments);
    }

    public Map<String, Vote> getVotes() {
        return new HashMap<>(votes);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question question = (Question) o;
        return Objects.equals(questionId, question.questionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(questionId);
    }

    @Override
    public String toString() {
        return "Question{" +
                "title='" + title + '\'' +
                ", author=" + author.getUsername() +
                ", votes=" + voteCount.get() +
                ", answers=" + answers.size() +
                ", views=" + viewCount.get() +
                ", status=" + status +
                '}';
    }
}

