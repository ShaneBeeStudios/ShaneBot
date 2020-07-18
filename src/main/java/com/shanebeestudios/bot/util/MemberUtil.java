package com.shanebeestudios.bot.util;

import com.shanebeestudios.bot.BotHandler;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;

public class MemberUtil {

    public static void muteMember(Member muted, long time, TimeFrame timeFrame, String reason, Member moderator) {
        // Add role
        Role role = BotHandler.getINSTANCE().getMutedRole();
        if (role != null) {
            muted.getGuild().addRoleToMember(muted, role).queue();
        }
        // Add to timer
        BotHandler.getINSTANCE().getMuteData().addMute(muted, parseTimespan(time, timeFrame));

        // Send message to bot channel
        TextChannel botChannel = BotHandler.getINSTANCE().getBotChannel();
        String name = BotHandler.getBot().getSelfUser().getName();

        MessageEmbed embed = new EmbedBuilder()
                .setTitle("-- MUTE TIME --")
                .setColor(Color.ORANGE)
                .setAuthor(name, null, Util.IMAGE_URL)
                .addField("Muted:", muted.getEffectiveName() + "(" + muted.getId() + ")", false)
                .addField("Time:", timeFrame.getFullTime(time), false)
                .addField("Reason:", reason, false)
                .addField("Moderator:", moderator.getEffectiveName(), false)
                .build();

        botChannel.sendMessage(embed).queue();

        // Send DM to muted
        name = name + " (" + botChannel.getGuild().getName() + ")";
        MessageEmbed toUser = new EmbedBuilder(embed)
                .setAuthor(name, null, Util.IMAGE_URL)
                .build();
        directMessage(muted, toUser);

        // Log to console
        Logger.info("MUTE TIME:");
        Logger.info(" - Muting: <blue>" + muted.getEffectiveName());
        Logger.info(" - Reason: <blue>" + reason);
        Logger.info(" - Time: <blue>" + timeFrame.getFullTime(time));
        Logger.info(" - Mod: <blue>" + moderator.getEffectiveName());
    }

    public static void unMuteMember(Member muted, String reason, Member moderator) {
        boolean auto = reason == null;

        // Remove role
        Role role = BotHandler.getINSTANCE().getMutedRole();
        if (role != null) {
            muted.getGuild().removeRoleFromMember(muted, role).queue();
        }

        // Remove from timer
        BotHandler.getINSTANCE().getMuteData().removeMute(muted.getId());

        // Send message to bot channel
        TextChannel botChannel = BotHandler.getINSTANCE().getBotChannel();
        String name = BotHandler.getBot().getSelfUser().getName();

        MessageEmbed embed = new EmbedBuilder()
                .setTitle("-- " + (auto ? "AUTO " : "") + "UN-MUTE --")
                .setColor(Color.GREEN)
                .setAuthor(name, null, Util.IMAGE_URL)
                .addField("UnMuted:", muted.getEffectiveName() + "(" + muted.getId() + ")", false)
                .build();

        if (auto) {
            botChannel.sendMessage(embed).queue();

            // Send DM to user
            name = name + " (" + botChannel.getGuild().getName() + ")";
            MessageEmbed toUser = new EmbedBuilder(embed)
                    .setAuthor(name, null, Util.IMAGE_URL)
                    .build();
            directMessage(muted, toUser);

            // Log to console
            Logger.info("AUTO UN-MUTE TIME:");
            Logger.info(" - Unmute: <green>" + muted.getEffectiveName());
        } else {
            MessageEmbed modEmbed = new EmbedBuilder(embed)
                    .addField("Reason:", reason, false)
                    .addField("Moderator:", moderator.getEffectiveName(), false)
                    .build();
            botChannel.sendMessage(modEmbed).queue();

            // Send DM to user
            name = name + " (" + botChannel.getGuild().getName() + ")";
            MessageEmbed toUser = new EmbedBuilder(modEmbed)
                    .setAuthor(name, null, Util.IMAGE_URL)
                    .build();
            directMessage(muted, toUser);

            // Log to console
            Logger.info("UN-MUTE TIME:");
            Logger.info(" - Unmute: <green>" + muted.getEffectiveName());
            Logger.info(" - Reason: <green>" + reason);
            Logger.info(" - Mod: <green>" + moderator.getEffectiveName());
        }
    }

    public static void directMessage(Member member, MessageEmbed message) {
        member.getUser().openPrivateChannel().queue(s -> s.sendMessage(message));
    }

    private static long parseTimespan(long time, TimeFrame timeFrame) {

        return (time * timeFrame.getMilliseconds()) + System.currentTimeMillis();
    }

}
