package com.shanebeestudios.bot.util;

import com.shanebeestudios.bot.BotHandler;

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
    private static final String BLUE = "\u001b[34;1m";
    private static final String PURPLE = "\u001B[35m";
    private static final String CYAN = "\u001B[36m";
    private static final String WHITE = "\u001B[37m";

    public static void info(String info) {
        log(format(Type.INFO) + info);
    }

    public static void info(String format, Object... objects) {
        info(String.format(format, objects));
    }

    public static void warn(String warning) {
        log(format(Type.WARN) + warning);
    }

    public static void warn(String format, Object... objects) {
        warn(String.format(format, objects));
    }

    public static void error(String error) {
        log(format(Type.ERROR) + error);
    }

    public static void error(String format, Object... objects) {
        error(String.format(format, objects));
    }

    private static void log(String message) {
        String newMessage = message
                .replace("<reset>", RESET).replace("<black>", BLACK).replace("<red>", RED)
                .replace("<green>", GREEN).replace("<yellow>", YELLOW).replace("<blue>", BLUE)
                .replace("<purple>", PURPLE).replace("<cyan>", CYAN).replace("<white>", WHITE);
        System.out.println(newMessage + RESET);
    }

    private static String threadName() {
        Thread thread = Thread.currentThread();
        String n = thread.getName();
        if (n.contains("JDA MainWS") || n.contains("main") || n.contains("Bot Loading")) {
            thread.setName(BotHandler.getBotName());
        }
        return thread.getName();
    }

    private static String format(Type type) {
        String time = DTF.format(LocalDateTime.now());
        return type.color + "[" + time + " " + CYAN + threadName() + type.color + " " + type.name + "]: ";
    }

    enum Type {
        INFO(RESET, "INFO"),
        WARN(YELLOW, "WARN"),
        ERROR(RED, "ERROR");

        private final String color;
        private final String name;

        Type(String color, String name) {
            this.color = color;
            this.name = name;
        }
    }

}
