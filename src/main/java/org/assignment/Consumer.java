package org.assignment;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Consumer implements Runnable {

    private static final Logger logger = Logger.getLogger(Consumer.class.getName());
    private final int id;
    private final BlockingQueue<Message> messageQueue;
    private final AtomicInteger successCount;
    private final AtomicInteger errorCount;

    public Consumer(int id, BlockingQueue<Message> queue, AtomicInteger successCount, AtomicInteger errorCount) {
        this.id = id;
        this.messageQueue = queue;
        this.successCount = successCount;
        this.errorCount = errorCount;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                Message msg = messageQueue.take();
                processMessage(msg);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.log(Level.WARNING, "Consumer interrupted", e);
        }
    }

    private void processMessage(Message msg) {
        try {
            logger.info(() -> "Consumer-" + id + " processing message: " + msg);

            // throw error on randomly
            if (Math.random() < 0.5) {
                throw new RuntimeException("Simulated processing error");
            }

            Thread.sleep(100);  // Processing time
            successCount.incrementAndGet();
            logger.info(() -> "Processed: " + msg);
        } catch (Exception e) {
            errorCount.incrementAndGet();
            logger.log(Level.WARNING, "Error processing message: " + msg, e);
        }
    }
}