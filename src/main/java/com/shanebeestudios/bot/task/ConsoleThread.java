package com.shanebeestudios.bot.task;

import com.shanebeestudios.bot.BotHandler;
import com.shanebeestudios.bot.util.Logger;

import java.io.IOException;
import java.util.Locale;
import java.util.Properties;
import java.util.Scanner;

public class ConsoleThread extends Thread {

    private final String version;

    @SuppressWarnings("CallToPrintStackTrace")
    public ConsoleThread(String name) {
        super(name);
        String version1 = "<unknown>";
        Properties properties = new Properties();
        try {
            properties.load(this.getContextClassLoader().getResourceAsStream("properties.properties"));
            version1 = properties.getProperty("version");
        } catch (IOException e) {
            e.printStackTrace();
        }
        version = version1;
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNextLine()) {
            String message = scanner.next();
            switch (message.toLowerCase(Locale.ROOT)) {
                case "help":
                    Logger.info("Available commands:<blue> stop, version");
                    break;
                case "stop":
                    Logger.info("Stopping server!");
                    BotHandler.getInstance().shutdown();
                    System.exit(0);
                    break;
                case "version":
                case "ver":
                    Logger.info("Running ShaneBot version: " + version);
                    break;
                default:
                    Logger.warn("Unknown command '%s', use 'help' for available commands", message);
            }
        }
    }

}
