package org.example.PubSub.enums;

/**
 * Enum representing the type of subscription in the pub-sub system.
 * Currently only supports GROUP subscriptions where messages are distributed
 * among consumers in a group using round-robin strategy.
 */
public enum SubscriptionType {
    /**
     * Group subscription - messages are distributed among consumers in a group
     * (only one consumer in the group processes each message)
     */
    GROUP
}
