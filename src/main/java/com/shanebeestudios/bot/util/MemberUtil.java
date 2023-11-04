package com.shanebeestudios.bot.util;

import com.shanebeestudios.bot.BotHandler;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.concurrent.TimeUnit;

public class MemberUtil {

    public static void muteMember(Member muted, long time, TimeUnit timeFrame, String reason, @Nullable Member mod) {
        // Mute member
        muted.getGuild().timeoutFor(muted, time, timeFrame).queue();

        // Send message to bot channel
        TextChannel botChannel = BotHandler.getInstance().getBotChannel();
        String botName = BotHandler.getBotName();

        MessageEmbed embed = new EmbedBuilder()
                .setTitle("-- MUTE TIME --")
                .setColor(Color.ORANGE)
                .setAuthor(botName, null, Util.IMAGE_URL)
                .addField("Muted:", muted.getEffectiveName() + "(" + muted.getId() + ")", false)
                .addField("Time:", time + " " + timeFrame, false)
                .addField("Reason:", reason, false)
                .addField("Moderator:", mod != null ? mod.getEffectiveName() : "<none>", false)
                .build();

        botChannel.sendMessageEmbeds(embed).queue();

        // Send DM to muted
        botName = botName + " (" + botChannel.getGuild().getName() + ")";
        MessageEmbed toUser = new EmbedBuilder(embed)
                .setAuthor(botName, null, Util.IMAGE_URL)
                .build();
        directMessage(muted, toUser);

        // Log to console
        Logger.info("MUTE TIME:");
        Logger.info(" - Muting: <blue>" + muted.getEffectiveName());
        Logger.info(" - Reason: <blue>" + reason);
        Logger.info(" - Time: <blue>" + time + " " + timeFrame);
        Logger.info(" - Mod: <blue>" + (mod != null ? mod.getEffectiveName() : "<none>"));
    }

    public static void mentionRemovalMessage(Member member, TextChannel channel) {
        TextChannel botChannel = BotHandler.getInstance().getBotChannel();
        String botName = BotHandler.getBotName();
        MessageEmbed embed = new EmbedBuilder()
                .setTitle("-- MENTION REMOVAL --")
                .setColor(Color.MAGENTA)
                .setAuthor(botName, null, Util.IMAGE_URL)
                .addField("Tagger:", member.getEffectiveName(), false)
                .addField("Channel:", channel.getName(), false)
                .build();
        botChannel.sendMessageEmbeds(embed).queue();

        // Log to console
        Logger.info("MENTION REMOVAL:");
        Logger.info(" - Tagger: <purple>" + member.getEffectiveName());
        Logger.info(" - Channel: <purple>" + channel.getName());
    }

    public static void directMessage(Member member, MessageEmbed message) {
        member.getUser().openPrivateChannel().queue(s -> s.sendMessageEmbeds(message).queue());
    }

}
