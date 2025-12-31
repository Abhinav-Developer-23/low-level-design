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
    
    private int userIdCounter;
    private int questionIdCounter;
    private int answerIdCounter;
    private int commentIdCounter;
    private int voteIdCounter;
    private int tagIdCounter;

    private StackOverflowSystem() {
        this.users = new ConcurrentHashMap<>();
        this.questions = new ConcurrentHashMap<>();
        this.answers = new ConcurrentHashMap<>();
        this.tags = new ConcurrentHashMap<>();
        this.observers = new CopyOnWriteArrayList<>();
        this.reputationStrategy = new DefaultReputationStrategy();
        
        this.userIdCounter = 0;
        this.questionIdCounter = 0;
        this.answerIdCounter = 0;
        this.commentIdCounter = 0;
        this.voteIdCounter = 0;
        this.tagIdCounter = 0;
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

    /**
     * //IMP
     * Registers an observer to receive notifications about system events.
     * Observers will be notified when questions are answered, comments are added,
     * answers are accepted, and votes are cast.
     * 
     * @param observer The observer to register for notifications
     */
    public void registerObserver(NotificationObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(NotificationObserver observer) {
        observers.remove(observer);
    }

    // User Management
    
    /**
     * //IMP
     * Creates a new user in the system with auto-generated unique ID.
     * The user is initialized with zero reputation and empty activity lists.
     * 
     * @param username The username for the new user
     * @param email The email address of the user
     * @param role The role of the user (MEMBER, MODERATOR, ADMIN)
     * @return The newly created User object with a unique ID
     */
    public User createUser(String username, String email, UserRole role) {
        String userId = "U" + (++userIdCounter);
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
    
    /**
     * //IMP
     * Creates a new tag or returns an existing one if a tag with the same name already exists.
     * Tag names are case-insensitive and automatically normalized to lowercase.
     * This prevents duplicate tags with different casing.
     * 
     * @param name The name of the tag (case-insensitive)
     * @param description A description of what the tag represents
     * @return The newly created Tag object or existing tag if name already exists
     */
    public Tag createTag(String name, String description) {
        // Check if tag already exists
        Optional<Tag> existingTag = tags.values().stream()
                .filter(t -> t.getName().equalsIgnoreCase(name))
                .findFirst();
        
        if (existingTag.isPresent()) {
            return existingTag.get();
        }
        
        String tagId = "T" + (++tagIdCounter);
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
    
    /**
     * //IMP
     * Posts a new question in the system with auto-generated unique ID.
     * The question is associated with the author and tagged with specified tags.
     * The author receives reputation points for posting the question.
     * 
     * @param author The user posting the question
     * @param title The title of the question
     * @param content The detailed content/body of the question
     * @param tags A set of tags to categorize the question
     * @return The newly created Question object with status OPEN
     */
    public Question postQuestion(User author, String title, String content, Set<Tag> tags) {
        String questionId = "Q" + (++questionIdCounter);
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
    
    /**
     * //IMP
     * Posts a new answer to an existing question with auto-generated unique ID.
     * The answer is linked to both the question and the author.
     * The author receives reputation points for posting the answer.
     * All registered observers are notified about the new answer.
     * 
     * @param author The user posting the answer
     * @param question The question being answered
     * @param content The content/body of the answer
     * @return The newly created Answer object
     */
    public Answer postAnswer(User author, Question question, String content) {
        String answerId = "A" + (++answerIdCounter);
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

    /**
     * Marks an answer as accepted by the question author.
     * Only the original question author can accept an answer to their question.
     * If another answer was previously accepted, it will be unmarked.
     * The answer author receives bonus reputation points for having their answer accepted.
     * 
     * @param answer The answer to be accepted
     * @param accepter The user accepting the answer (must be the question author)
     * @throws IllegalStateException if the accepter is not the question author
     */
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
    
    /**
     * //IMP
     * Casts a vote (upvote or downvote) on a question.
     * If the user previously voted, the old vote is replaced with the new one.
     * The question author's reputation is adjusted based on the vote type.
     * Upvotes increase reputation, downvotes decrease it.
     * All registered observers are notified about the vote.
     * 
     * @param voter The user casting the vote
     * @param question The question being voted on
     * @param voteType The type of vote (UPVOTE or DOWNVOTE)
     */
    public void voteOnQuestion(User voter, Question question, VoteType voteType) {
        String voteId = "V" + (++voteIdCounter);
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

    /**
     * //IMP
     * Casts a vote (upvote or downvote) on an answer.
     * If the user previously voted, the old vote is replaced with the new one.
     * The answer author's reputation is adjusted based on the vote type.
     * Upvotes increase reputation, downvotes decrease it.
     * All registered observers are notified about the vote.
     * 
     * @param voter The user casting the vote
     * @param answer The answer being voted on
     * @param voteType The type of vote (UPVOTE or DOWNVOTE)
     */
    public void voteOnAnswer(User voter, Answer answer, VoteType voteType) {
        String voteId = "V" + (++voteIdCounter);
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
    
    /**
     * //IMP
     * Adds a comment to a question with auto-generated unique ID.
     * The comment is linked to both the question and the author.
     * All registered observers are notified about the new comment.
     * 
     * @param author The user posting the comment
     * @param question The question being commented on
     * @param content The content/text of the comment
     * @return The newly created Comment object
     */
    public Comment commentOnQuestion(User author, Question question, String content) {
        String commentId = "C" + (++commentIdCounter);
        Comment comment = new Comment(commentId, author, content);
        
        question.addComment(comment);
        author.addComment(comment);
        
        // Notify observers
        notifyQuestionCommented(question, comment, author);
        
        return comment;
    }

    /**
     * //IMP
     * Adds a comment to an answer with auto-generated unique ID.
     * The comment is linked to both the answer and the author.
     * All registered observers are notified about the new comment.
     * 
     * @param author The user posting the comment
     * @param answer The answer being commented on
     * @param content The content/text of the comment
     * @return The newly created Comment object
     */
    public Comment commentOnAnswer(User author, Answer answer, String content) {
        String commentId = "C" + (++commentIdCounter);
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

