package com.shanebeestudios.bot.command;

import com.shanebeestudios.bot.BotHandler;
import com.shanebeestudios.bot.util.Util;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;
import java.util.Date;
import java.util.Random;

public class Release extends Command {

    public Release(Permission permission) {
        super(permission);
        this.description = "Release a plugin update";
        this.usage = "!release <plugin> <version> <link> <description>";
    }

    @Override
    public boolean run() {
        message.delete().queue();
        if (args.length >= 4) {
            Random random = new Random();
            Color color = new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255));
            String plugin = args[0];
            String version = args[1];
            String link = args[2];

            StringBuilder description = new StringBuilder();
            for (int i = 3; i < args.length; i++) {
                description.append(args[i]).append(" ");
            }

            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setColor(color);
            embedBuilder.setThumbnail(Util.IMAGE_URL);
            embedBuilder.setAuthor("Plugin Release", null, Util.IMAGE_URL);
            embedBuilder.setTitle("**" + plugin + " - " + version + "**");
            embedBuilder.setDescription(description);
            embedBuilder.addField("**Download:**", link, false);
            embedBuilder.setFooter(BotHandler.getBotName(), BotHandler.getBot().getSelfUser().getAvatarUrl());
            embedBuilder.setTimestamp(new Date(System.currentTimeMillis()).toInstant());

            channel.sendMessage(embedBuilder.build()).queue();
        } else {
            channel.sendMessage("**Incorrect Arguments:** " + getUsage()).queue();
        }
        return true;
    }

}
