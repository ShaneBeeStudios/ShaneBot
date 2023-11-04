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

    /**
     * Timeout a member
     * <p>This will also log to the bot channel, log to console and send a DM to the user</p>
     *
     * @param muted    Member to be timedout
     * @param time     Time to timeout for
     * @param timeUnit TimeUnit to timeout for
     * @param reason   Reason for the timeout
     * @param mod      Moderator who performed the timeout
     */
    public static void timeoutMember(Member muted, long time, TimeUnit timeUnit, String reason, @Nullable Member mod) {
        // Mute member
        muted.getGuild().timeoutFor(muted, time, timeUnit).queue();

        // Send message to bot channel
        BotHandler botHandler = BotHandler.getInstance();
        TextChannel botChannel = botHandler.getBotChannel();
        String botName = botHandler.getBotName();

        MessageEmbed embed = new EmbedBuilder()
                .setTitle("-- TIMEOUT TIME --")
                .setColor(Color.RED)
                .setAuthor(botName, null, Util.IMAGE_URL)
                .addField("Muted:", muted.getEffectiveName() + "(" + muted.getId() + ")", false)
                .addField("Time:", time + " " + timeUnit, false)
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
        Logger.info("TIMEOUT TIME:");
        Logger.info(" - Muting: <blue>" + muted.getEffectiveName());
        Logger.info(" - Reason: <blue>" + reason);
        Logger.info(" - Time: <blue>" + time + " " + timeUnit);
        Logger.info(" - Mod: <blue>" + (mod != null ? mod.getEffectiveName() : "<none>"));
    }

    public static void mentionRemovalMessage(Member member, TextChannel channel) {
        BotHandler botHandler = BotHandler.getInstance();
        TextChannel botChannel = botHandler.getBotChannel();
        String botName = botHandler.getBotName();
        MessageEmbed embed = new EmbedBuilder()
                .setTitle("-- MENTION REMOVAL --")
                .setColor(Color.ORANGE)
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

    private static void directMessage(Member member, MessageEmbed message) {
        member.getUser().openPrivateChannel().queue(s -> s.sendMessageEmbeds(message).queue());
    }

}
