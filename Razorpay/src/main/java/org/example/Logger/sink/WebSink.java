package org.example.Logger.sink;

import org.example.Logger.interfaces.LogSink;
import org.example.Logger.model.LogMessage;

/**
 * Web sink implementation - simulates sending logs to a web endpoint
 * In a real-world scenario, this would make HTTP requests to a logging service
 */
public class WebSink implements LogSink {
    private final String endpoint;

    public WebSink(String endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    public void write(LogMessage logMessage) {
        // Simulate sending log to web endpoint
        // In production, this would use HttpClient or similar
        System.out.println("[WEB] Sending to " + endpoint + ": " + logMessage.formatMessage());
        
        // Simulated HTTP POST logic
        simulateWebRequest(logMessage);
    }

    private void simulateWebRequest(LogMessage logMessage) {
        // In a real implementation, this would be:
        // HttpClient client = HttpClient.newHttpClient();
        // HttpRequest request = HttpRequest.newBuilder()
        //     .uri(URI.create(endpoint))
        //     .POST(HttpRequest.BodyPublishers.ofString(toJson(logMessage)))
        //     .build();
        // client.send(request, HttpResponse.BodyHandlers.ofString());
        
        // For now, we'll just simulate the action
        try {
            Thread.sleep(10); // Simulate network delay
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public String getSinkType() {
        return "WEB";
    }

    public String getEndpoint() {
        return endpoint;
    }
}





