package com.shanebeestudios.bot.command;

import com.shanebeestudios.bot.BotHandler;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;

public class Ban extends Command {

    @Override
    public boolean run() {
        if (args.length >= 2) {
            Member banned = parseMember(0);
            if (banned == null) {
                channel.sendMessage("Invalid Member: " + args[0]).queue();
                return true;
            }

            StringBuilder reason = new StringBuilder();
            for (int i = 1; i < args.length; i++) {
                reason.append(args[i]).append(" ");
            }
            banMessage(banned, reason.toString());
            //channel.getGuild().ban(banned, 1, reason.toString()); TODO will test first before actually banning anyone
        }
        return true;
    }

    private void banMessage(Member banned, String reason) {
        TextChannel botChannel = BotHandler.getINSTANCE().getBotChannel();

        MessageEmbed embed = new EmbedBuilder()
                .setTitle("-- Ban TIME --")
                .setColor(Color.RED)
                .setAuthor("ShaneBot", "https://i.imgur.com/VbV0p7j.png", "https://i.imgur.com/VbV0p7j.png")
                .addField("Banned:", banned.getEffectiveName() + "(" + banned.getId() + ")", false)
                .addField("Reason:", reason, false)
                .addField("Moderator:", member.getEffectiveName(), false)
                .build();

        botChannel.sendMessage(embed).queue();
    }

}
