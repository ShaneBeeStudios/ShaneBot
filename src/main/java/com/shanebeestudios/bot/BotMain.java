package com.shanebeestudios.bot;

import com.shanebeestudios.bot.util.Logger;
import joptsimple.OptionParser;
import joptsimple.OptionSet;

import java.io.IOException;

public class BotMain {

    static BotHandler botHandler;

    public static void main(String[] args) {
        OptionParser parser = new OptionParser();

        parser.accepts("help", "Shows help and exits");
        parser.accepts("token", "The token for the bot")
                .withRequiredArg()
                .ofType(String.class);
        parser.accepts("server", "ID of server")
                .withRequiredArg()
                .ofType(String.class);
        parser.accepts("welcome-c", "ID of WELCOME channel")
                .withRequiredArg()
                .ofType(String.class);
        parser.accepts("rules-c", "ID of RULES channel")
                .withRequiredArg()
                .ofType(String.class);
        parser.accepts("bot-c", "ID of bot/punishment channel")
                .withRequiredArg()
                .ofType(String.class);
        parser.accepts("muted-r", "ID of MUTED role")
                .withRequiredArg()
                .ofType(String.class);

        OptionSet options = parser.parse(args);

        if (options.has("help")) {
            try {
                parser.printHelpOn(System.out);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.exit(0);
        }

        if (!options.has("token")) {
            Logger.error("No token specified, shutting down...");
            System.exit(1);
        }
        if (!options.has("server")) {
            Logger.error("No server ID specified, shutting down...");
            System.exit(1);
        }
        if (!options.has("welcome-c")) {
            Logger.error("No welcome channel ID specified, shutting down...");
            System.exit(1);
        }
        if (!options.has("rules-c")) {
            Logger.error("No rules channel ID specified, shutting down...");
            System.exit(1);
        }
        if (!options.has("bot-c")) {
            Logger.error("No bot channel ID specified, shutting down...");
            System.exit(1);
        }
        if (!options.has("muted-r")) {
            Logger.error("No muted role ID specified, shutting down...");
            System.exit(1);
        }

        String token = (String) options.valueOf("token");
        String server = (String) options.valueOf("server");
        String welcomeC = (String) options.valueOf("welcome-c");
        String rulesC = (String) options.valueOf("rules-c");
        String botC = (String) options.valueOf("bot-c");
        String mutedR = (String) options.valueOf("muted-r");

        botHandler = new BotHandler(token, server, welcomeC, rulesC, botC, mutedR);
    }

}
