package com.shanebeestudios.bot.command;

import com.shanebeestudios.bot.BotHandler;
import com.shanebeestudios.bot.util.Util;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;
import java.util.Map;
import java.util.Random;

public class Commands extends Command {

    private final BotHandler botHandler;

    public Commands(boolean requiresAdmin, BotHandler botHandler) {
        super(requiresAdmin);
        this.botHandler = botHandler;
    }

    @Override
    public boolean run() {
        Random random = new Random();
        Color color = new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255));
        String name = BotHandler.getBot().getSelfUser().getName();

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("-- COMMANDS --")
                .setColor(color)
                .setAuthor(name, null, Util.IMAGE_URL);

        Map<String, Command> commands = botHandler.getCommands();
        for (String c : commands.keySet()) {
            Command command = commands.get(c);
            if (command != this && !c.equalsIgnoreCase("test")) {
                embedBuilder.addField("**__" + c + "__**:", command.getDescription() + System.lineSeparator() + "```" + command.getUsage() + "```", false);
            }
        }
        message.delete().queue();
        channel.sendMessage(embedBuilder.build()).queue();

        return true;
    }

}
