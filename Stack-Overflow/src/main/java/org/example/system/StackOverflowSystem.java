package org.example.system;

import lombok.Setter;
import org.example.enums.QuestionStatus;
import org.example.enums.UserRole;
import org.example.enums.VoteType;
import org.example.interfaces.NotificationObserver;
import org.example.interfaces.ReputationStrategy;
import org.example.interfaces.SearchStrategy;
import org.example.model.*;
import org.example.strategies.reputation.DefaultReputationStrategy;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Singleton Pattern: Single instance of the Stack Overflow system
 * Thread-safe implementation using double-checked locking
 */
public class StackOverflowSystem {
    
    private static volatile StackOverflowSystem instance;
    
    private final Map<String, User> users;
    private final Map<String, Question> questions;
    private final Map<String, Answer> answers;
    private final Map<String, Tag> tags;
    private final List<NotificationObserver> observers;

    // Strategy Pattern: Allow changing reputation calculation strategy
    @Setter
    private ReputationStrategy reputationStrategy;
    
    private final AtomicLong userIdCounter;
    private final AtomicLong questionIdCounter;
    private final AtomicLong answerIdCounter;
    private final AtomicLong commentIdCounter;
    private final AtomicLong voteIdCounter;
    private final AtomicLong tagIdCounter;

    private StackOverflowSystem() {
        this.users = new ConcurrentHashMap<>();
        this.questions = new ConcurrentHashMap<>();
        this.answers = new ConcurrentHashMap<>();
        this.tags = new ConcurrentHashMap<>();
        this.observers = new CopyOnWriteArrayList<>();
        this.reputationStrategy = new DefaultReputationStrategy();
        
        this.userIdCounter = new AtomicLong(0);
        this.questionIdCounter = new AtomicLong(0);
        this.answerIdCounter = new AtomicLong(0);
        this.commentIdCounter = new AtomicLong(0);
        this.voteIdCounter = new AtomicLong(0);
        this.tagIdCounter = new AtomicLong(0);
    }

    /**
     * Thread-safe singleton implementation using double-checked locking
     */
    public static StackOverflowSystem getInstance() {
        if (instance == null) {
            synchronized (StackOverflowSystem.class) {
                if (instance == null) {
                    instance = new StackOverflowSystem();
                }
            }
        }
        return instance;
    }

    // Observer Pattern: Register observers


    //#imp
    public void registerObserver(NotificationObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(NotificationObserver observer) {
        observers.remove(observer);
    }

    // User Management
    public User createUser(String username, String email, UserRole role) {
        String userId = "U" + userIdCounter.incrementAndGet();
        User user = new User(userId, username, email, role);
        users.put(userId, user);
        return user;
    }

    public User getUser(String userId) {
        return users.get(userId);
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    // Tag Management
    public Tag createTag(String name, String description) {
        // Check if tag already exists
        Optional<Tag> existingTag = tags.values().stream()
                .filter(t -> t.getName().equalsIgnoreCase(name))
                .findFirst();
        
        if (existingTag.isPresent()) {
            return existingTag.get();
        }
        
        String tagId = "T" + tagIdCounter.incrementAndGet();
        Tag tag = new Tag(tagId, name, description);
        tags.put(tagId, tag);
        return tag;
    }

    public Tag getTag(String tagId) {
        return tags.get(tagId);
    }

    public List<Tag> getAllTags() {
        return new ArrayList<>(tags.values());
    }

    // Question Operations
    public Question postQuestion(User author, String title, String content, Set<Tag> tags) {
        String questionId = "Q" + questionIdCounter.incrementAndGet();
        Question question = new Question(questionId, author, title, content, tags);
        
        questions.put(questionId, question);
        author.addQuestion(question);
        
        // Award reputation for posting question
        int reputation = reputationStrategy.calculateQuestionPostedReputation();
        author.addReputation(reputation);
        
        return question;
    }

    public Question getQuestion(String questionId) {
        Question question = questions.get(questionId);
        if (question != null) {
            question.incrementViewCount();
        }
        return question;
    }

    public List<Question> getAllQuestions() {
        return new ArrayList<>(questions.values());
    }

    public List<Question> getQuestionsByStatus(QuestionStatus status) {
        return questions.values().stream()
                .filter(q -> q.getStatus() == status)
                .toList();
    }

    // Answer Operations
    public Answer postAnswer(User author, Question question, String content) {
        String answerId = "A" + answerIdCounter.incrementAndGet();
        Answer answer = new Answer(answerId, author, content, question);
        
        answers.put(answerId, answer);
        question.addAnswer(answer);
        author.addAnswer(answer);
        
        // Award reputation for posting answer
        int reputation = reputationStrategy.calculateAnswerPostedReputation();
        author.addReputation(reputation);
        
        // Notify observers
        notifyQuestionAnswered(question, answer, author);
        
        return answer;
    }

    public Answer getAnswer(String answerId) {
        return answers.get(answerId);
    }

    public void acceptAnswer(Answer answer, User accepter) {
        // Only question author can accept answer
        if (!answer.getQuestion().getAuthor().equals(accepter)) {
            throw new IllegalStateException("Only the question author can accept an answer");
        }
        
        // Unmark any previously accepted answer
        answer.getQuestion().getAcceptedAnswer().ifPresent(Answer::unmarkAsAccepted);
        
        // Mark the new answer as accepted
        answer.markAsAccepted();
        
        // Award reputation to answer author
        int reputation = reputationStrategy.calculateAcceptedAnswerReputation();
        answer.getAuthor().addReputation(reputation);
        
        // Notify observers
        notifyAnswerAccepted(answer, accepter);
    }

    // Voting Operations
    public void voteOnQuestion(User voter, Question question, VoteType voteType) {
        String voteId = "V" + voteIdCounter.incrementAndGet();
        Vote vote = new Vote(voteId, voter, voteType);
        
        VoteType previousVote = question.getUserVote(voter.getUserId());
        question.addVote(vote);
        
        // Award/deduct reputation based on vote change
        if (previousVote != voteType) {
            // Remove old reputation if there was a previous vote
            if (previousVote != null) {
                int oldReputation = reputationStrategy.calculateQuestionVoteReputation(previousVote);
                question.getAuthor().addReputation(-oldReputation);
            }
            
            // Add new reputation
            int newReputation = reputationStrategy.calculateQuestionVoteReputation(voteType);
            question.getAuthor().addReputation(newReputation);
        }
        
        // Notify observers
        notifyQuestionVoted(question, voter);
    }

    public void voteOnAnswer(User voter, Answer answer, VoteType voteType) {
        String voteId = "V" + voteIdCounter.incrementAndGet();
        Vote vote = new Vote(voteId, voter, voteType);
        
        VoteType previousVote = answer.getUserVote(voter.getUserId());
        answer.addVote(vote);
        
        // Award/deduct reputation based on vote change
        if (previousVote != voteType) {
            // Remove old reputation if there was a previous vote
            if (previousVote != null) {
                int oldReputation = reputationStrategy.calculateAnswerVoteReputation(previousVote);
                answer.getAuthor().addReputation(-oldReputation);
            }
            
            // Add new reputation
            int newReputation = reputationStrategy.calculateAnswerVoteReputation(voteType);
            answer.getAuthor().addReputation(newReputation);
        }
        
        // Notify observers
        notifyAnswerVoted(answer, voter);
    }

    // Comment Operations
    public Comment commentOnQuestion(User author, Question question, String content) {
        String commentId = "C" + commentIdCounter.incrementAndGet();
        Comment comment = new Comment(commentId, author, content);
        
        question.addComment(comment);
        author.addComment(comment);
        
        // Notify observers
        notifyQuestionCommented(question, comment, author);
        
        return comment;
    }

    public Comment commentOnAnswer(User author, Answer answer, String content) {
        String commentId = "C" + commentIdCounter.incrementAndGet();
        Comment comment = new Comment(commentId, author, content);
        
        answer.addComment(comment);
        author.addComment(comment);
        
        // Notify observers
        notifyAnswerCommented(answer, comment, author);
        
        return comment;
    }

    // Search Operations using Strategy Pattern
    public List<Question> searchQuestions(String query, SearchStrategy strategy) {
        List<Question> allQuestions = getAllQuestions();
        return strategy.search(query, allQuestions);
    }

    // Notification methods (Observer Pattern)
    private void notifyQuestionAnswered(Question question, Answer answer, User answerer) {
        for (NotificationObserver observer : observers) {
            observer.onQuestionAnswered(question.getQuestionId(), answer.getAnswerId(), answerer);
        }
    }

    private void notifyAnswerAccepted(Answer answer, User accepter) {
        for (NotificationObserver observer : observers) {
            observer.onAnswerAccepted(answer.getAnswerId(), accepter);
        }
    }

    private void notifyQuestionCommented(Question question, Comment comment, User commenter) {
        for (NotificationObserver observer : observers) {
            observer.onQuestionCommented(question.getQuestionId(), comment.getCommentId(), commenter);
        }
    }

    private void notifyAnswerCommented(Answer answer, Comment comment, User commenter) {
        for (NotificationObserver observer : observers) {
            observer.onAnswerCommented(answer.getAnswerId(), comment.getCommentId(), commenter);
        }
    }

    private void notifyQuestionVoted(Question question, User voter) {
        for (NotificationObserver observer : observers) {
            observer.onQuestionVoted(question.getQuestionId(), voter);
        }
    }

    private void notifyAnswerVoted(Answer answer, User voter) {
        for (NotificationObserver observer : observers) {
            observer.onAnswerVoted(answer.getAnswerId(), voter);
        }
    }

    // Utility methods
    public void displayQuestionDetails(Question question) {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("Question: " + question.getTitle());
        System.out.println("=".repeat(80));
        System.out.println("Author: " + question.getAuthor().getUsername() + 
                         " (Rep: " + question.getAuthor().getReputation() + ")");
        System.out.println("Tags: " + question.getTags());
        System.out.println("Votes: " + question.getVoteCount() + " | Views: " + question.getViewCount());
        System.out.println("Status: " + question.getStatus());
        System.out.println("\n" + question.getContent());
        
        if (!question.getComments().isEmpty()) {
            System.out.println("\nComments:");
            for (Comment comment : question.getComments()) {
                System.out.println("  - " + comment);
            }
        }
        
        if (!question.getAnswers().isEmpty()) {
            System.out.println("\n" + "-".repeat(80));
            System.out.println("Answers (" + question.getAnswers().size() + "):");
            System.out.println("-".repeat(80));
            
            for (Answer answer : question.getAnswers()) {
                System.out.println("\nBy: " + answer.getAuthor().getUsername() + 
                                 " (Rep: " + answer.getAuthor().getReputation() + ")");
                System.out.println("Votes: " + answer.getVoteCount() + 
                                 (answer.isAccepted() ? " [ACCEPTED]" : ""));
                System.out.println(answer.getContent());
                
                if (!answer.getComments().isEmpty()) {
                    System.out.println("Comments:");
                    for (Comment comment : answer.getComments()) {
                        System.out.println("  - " + comment);
                    }
                }
            }
        }
        System.out.println("=".repeat(80) + "\n");
    }
}

