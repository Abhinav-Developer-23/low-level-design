package org.example.PubSub.model;

import lombok.Getter;
import lombok.Setter;
import org.example.PubSub.enums.MessageStatus;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents a message in the pub-sub system.
 * Messages contain data that publishers send to topics and consumers receive.
 */
@Getter
public class Message {
    // Getters
    private final String messageId;
    private final String topicId;
    private final String payload;
    private final LocalDateTime timestamp;
    // Setters
    @Setter
    private MessageStatus status;
    @Setter
    private String processedBy; // Consumer ID or ConsumerGroup ID that processed this message

    /**
     * Constructor to create a new message.
     *
     * @param topicId The ID of the topic this message belongs to
     * @param payload The actual data/content of the message (must be a String)
     */
    public Message(String topicId, String payload) {
        this.messageId = UUID.randomUUID().toString();
        this.topicId = topicId;
        this.payload = payload;
        this.timestamp = LocalDateTime.now();
        this.status = MessageStatus.PENDING;
        this.processedBy = null;
    }

    @Override
    public String toString() {
        return "Message{" +
                "messageId='" + messageId + '\'' +
                ", topicId='" + topicId + '\'' +
                ", payload=" + payload +
                ", timestamp=" + timestamp +
                ", status=" + status +
                ", processedBy='" + processedBy + '\'' +
                '}';
    }
}
