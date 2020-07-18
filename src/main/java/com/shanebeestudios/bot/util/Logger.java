package com.shanebeestudios.bot.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {

    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("HH:mm:ss");
    // Colors for messages
    private static final String RESET = "\u001B[0m";
    private static final String BLACK = "\u001B[30m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE = "\u001B[34m";
    private static final String PURPLE = "\u001B[35m";
    private static final String CYAN = "\u001B[36m";
    private static final String WHITE = "\u001B[37m";

    public static void info(String info) {
        LocalDateTime now = LocalDateTime.now();
        String th = Thread.currentThread().getName();
        log("[" + DTF.format(now) + " " + CYAN + th + RESET + " INFO" + "]: " + info + RESET);
    }

    public static void warn(String warning) {
        LocalDateTime now = LocalDateTime.now();
        String th = Thread.currentThread().getName();
        log(YELLOW + "[" + DTF.format(now) + " " + CYAN + th + YELLOW + " WARN" + "]: " + warning + RESET);
    }

    public static void error(String error) {
        LocalDateTime now = LocalDateTime.now();
        String th = Thread.currentThread().getName();
        log(RED + "[" + DTF.format(now) + " " + CYAN + th + RED + " ERROR" + "]: " + error + RESET);
    }

    private static void log(String message) {
        String newMessage = message
                .replace("<reset>", RESET).replace("<black>", BLACK).replace("<red>", RED)
                .replace("<green>", GREEN).replace("<yellow>", YELLOW).replace("<blue>", BLUE)
                .replace("<purple>", PURPLE).replace("<cyan>", CYAN).replace("<white>", WHITE);
        System.out.println(newMessage);
    }

}
