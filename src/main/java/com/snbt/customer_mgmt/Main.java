package com.snbt.customer_mgmt;

import com.snbt.customer_mgmt.bl.InMemoryDBService;
import com.snbt.customer_mgmt.ws.CustomerController;

public class Main {

    public static void main(String[] args) {
        Application.start();

        addShutdownHook();
    }


    public static class Application {
        private static InMemoryDBService customerDBService;

        public static void start() {
            InMemoryDBService customerDBService = new InMemoryDBService();
            customerDBService.start();
            Application.customerDBService = customerDBService;
            CustomerController.go(Application.customerDBService);
        }

        public static void stop() {
            customerDBService.stop();
            CustomerController.stop();
        }
    }

    private static void addShutdownHook() {
        final Thread mainThread = Thread.currentThread();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                Application.stop();
                mainThread.join();
            } catch (InterruptedException e) {
                mainThread.interrupt();
            }
        }));
    }
}
