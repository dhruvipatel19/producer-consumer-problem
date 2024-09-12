package org.assignment;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class ProduceConsumeApp {

    private static final Logger logger = Logger.getLogger(ProduceConsumeApp.class.getName());

    public static final int QUEUE_CAPACITY = 100;
    public static final int PRODUCER_COUNT = 3; // number of producer
    public static final int CONSUMER_COUNT = 2; // number of consumer
    public static final int LIMIT = 10; // Message per producer limit

    // To store the message in BlockingQueue which is thread safe
    private final BlockingQueue<Message> messageQueue;

    // Atomic integer used as it synchronized
    private final AtomicInteger successCount;  // keep track of success count
    private final AtomicInteger errorCount; // keep track of error count

    public ProduceConsumeApp() {
        this.messageQueue = new LinkedBlockingQueue<>(QUEUE_CAPACITY);
        this.successCount = new AtomicInteger(0);
        this.errorCount = new AtomicInteger(0);
    }

    public void run() {
        // create producer threads
        for (int i = 0; i < PRODUCER_COUNT; i++) {
            new Thread(new Producer(i, messageQueue)).start();
        }

        // create consumer threads
        for (int i = 0; i < CONSUMER_COUNT; i++) {
            new Thread(new Consumer(i, messageQueue, successCount, errorCount)).start();
        }
    }

    public int getSuccessCount() {
        return successCount.get();
    }

    public int getErrorCount() {
        return errorCount.get();
    }

    public static void main(String[] args) throws InterruptedException {
        ProduceConsumeApp app = new ProduceConsumeApp();
        app.run();

        Thread.sleep(5000);

        logger.info(() -> "Successful count: " + app.getSuccessCount());
        logger.info(() -> "Error count: " + app.getErrorCount());
    }
}

