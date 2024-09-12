package org.assignment;

import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Producer implements Runnable {
    private static final Logger logger = Logger.getLogger(Producer.class.getName());
    private final int id;
    private final BlockingQueue<Message> messageQueue;

    public Producer(int id, BlockingQueue<Message> messageQueue) {
        this.id = id;
        this.messageQueue = messageQueue;
    }

    @Override
    public void run() {

        try {
            for (int i = 0; i < ProduceConsumeApp.LIMIT; i++) {
                Message msg = new Message("Producer-" + id + " Message-" + i);
                messageQueue.put(msg);
                logger.info(() -> "Produced: " + msg);
                Thread.sleep(50);  // production time
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.log(Level.WARNING, "Producer interrupted", e);
        }
    }
}
