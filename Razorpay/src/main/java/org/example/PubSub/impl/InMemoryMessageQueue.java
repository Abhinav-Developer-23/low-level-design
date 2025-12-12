package org.example.PubSub.impl;

import org.example.PubSub.interfaces.IMessageQueue;
import org.example.PubSub.model.Message;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * In-memory implementation of IMessageQueue using ConcurrentLinkedQueue
 * Thread-safe implementation for concurrent producer-consumer scenarios
 * Follows Dependency Inversion Principle (DIP)
 */
public class InMemoryMessageQueue implements IMessageQueue {
    private final Queue<Message> queue;
    
    public InMemoryMessageQueue() {
        this.queue = new ConcurrentLinkedQueue<>();
    }
    
    @Override
    public void enqueue(Message message) {
        if (message == null) {
            throw new IllegalArgumentException("Message cannot be null");
        }
        queue.offer(message);
    }
    
    @Override
    public Message dequeue() {
        return queue.poll();
    }
    
    @Override
    public Message peek() {
        return queue.peek();
    }
    
    @Override
    public boolean isEmpty() {
        return queue.isEmpty();
    }
    
    @Override
    public int size() {
        return queue.size();
    }
}

