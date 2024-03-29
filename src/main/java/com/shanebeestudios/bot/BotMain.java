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
                .withRequiredArg().ofType(String.class);
        parser.accepts("server", "ID of server")
                .withRequiredArg().ofType(String.class);
        parser.accepts("bot-c", "ID of bot/punishment channel")
                .withRequiredArg().ofType(String.class);
        parser.accepts("admin-r", "ID of ADMIN role")
                .withRequiredArg().ofType(String.class);

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
        if (!options.has("bot-c")) {
            Logger.error("No bot channel ID specified, shutting down...");
            System.exit(1);
        }
        if (!options.has("admin-r")) {
            Logger.error("No admin role ID specified, shutting down...");
            System.exit(1);
        }

        String token = (String) options.valueOf("token");
        String serverID = (String) options.valueOf("server");
        String botChannelID = (String) options.valueOf("bot-c");
        String adminRoleID = (String) options.valueOf("admin-r");

        botHandler = new BotHandler(token, serverID, botChannelID, adminRoleID);
    }

}
