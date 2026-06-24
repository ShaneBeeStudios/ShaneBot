package com.shanebeestudios.bot.util;

import com.shanebeestudios.bot.BotHandler;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.utils.FileUpload;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.List;
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

    /**
     * Called when a member has been flagged for image-spam scam behaviour.
     * <p>Times the member out for 24 hours and posts an alert in the bot channel for admins to review.</p>
     * <p>The downloaded spam images are attached directly to the alert message so they remain
     * visible for review even after the original messages are deleted.</p>
     *
     * @param spammer      The member who was flagged.
     * @param channelCount How many distinct channels they posted in during the detection window.
     * @param imageUploads Downloaded copies of the spam images to attach to the alert.
     */
    public static void scamSpamDetected(Member spammer, int channelCount, List<FileUpload> imageUploads) {
        Logger.info("SCAM SPAM DETECTED:");
        Logger.info(" - User: <red>" + spammer.getEffectiveName() + " (" + spammer.getId() + ")");
        Logger.info(" - Channels spammed: <red>" + channelCount);

        BotHandler botHandler = BotHandler.getInstance();
        TextChannel botChannel = botHandler.getBotChannel();
        String botName = botHandler.getBotName();

        if (botChannel == null) {
            Logger.error("scamSpamDetected: botChannel is null! Check the --bot-c ID passed at startup.");
            return;
        }

        // Attempt timeout — skip gracefully for guild owner or members with a higher role than the bot
        boolean isOwner = spammer.isOwner();
        boolean canTimeout = !isOwner && spammer.getGuild().getSelfMember().canInteract(spammer);
        if (canTimeout) {
            spammer.getGuild().timeoutFor(spammer, 24, TimeUnit.HOURS).queue(
                    success -> Logger.info(" - Timeout applied successfully"),
                    err -> Logger.warn(" - Timeout failed: " + err.getMessage())
            );
        } else {
            Logger.warn(" - Skipping timeout: cannot moderate " + spammer.getEffectiveName()
                    + (isOwner ? " (guild owner)" : " (higher role than bot)"));
        }

        // Reference the first uploaded file in the embed image so it renders inline
        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setTitle("\uD83D\uDEA8 SCAM SPAM DETECTED \uD83D\uDEA8")
                .setColor(Color.RED)
                .setAuthor(botName, null, Util.IMAGE_URL)
                .addField("User:", spammer.getEffectiveName() + " (" + spammer.getId() + ")", false)
                .addField("Channels spammed:", String.valueOf(channelCount), false)
                .addField("Action taken:", canTimeout ? "Timed out for 24 hours" : "Could not timeout (owner or higher role)", false)
                .addField("Next steps:", "Review the user's recent messages and ban if this is a scam account.", false)
                .setFooter("User ID: " + spammer.getId());

        // Use attachment:// scheme to reference the first uploaded file inline in the embed
        if (!imageUploads.isEmpty()) {
            embedBuilder.setImage("attachment://" + imageUploads.get(0).getName());
        }

        botChannel.sendMessageEmbeds(embedBuilder.build())
                .addFiles(imageUploads)
                .queue(
                        success -> Logger.info(" - Alert posted to bot channel"),
                        err -> Logger.error(" - Failed to post to bot channel: " + err.getMessage())
                );

        // DM the flagged user so they know what happened
        // (useful if it's a compromised account whose owner may notice)
        if (canTimeout) {
            MessageEmbed toUser = new EmbedBuilder()
                    .setTitle("You have been timed out")
                    .setColor(Color.RED)
                    .setAuthor(botName + " (" + botChannel.getGuild().getName() + ")", null, Util.IMAGE_URL)
                    .addField("Reason:", "Suspected scam image spam \u2014 posting images across multiple channels in quick succession.", false)
                    .addField("Duration:", "24 hours", false)
                    .addField("Note:", "If your account was compromised, please secure it and contact a server admin.", false)
                    .build();
            directMessage(spammer, toUser);
        }

        Logger.info(" - Action: <red>Timed out 24 hours, admins notified");
    }

    private static void directMessage(Member member, MessageEmbed message) {
        member.getUser().openPrivateChannel().queue(s -> s.sendMessageEmbeds(message).queue());
    }

}
