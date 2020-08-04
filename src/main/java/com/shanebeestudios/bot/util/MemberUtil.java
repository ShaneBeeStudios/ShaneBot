package com.shanebeestudios.bot.util;

import com.shanebeestudios.bot.BotHandler;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

public class MemberUtil {

    public static void muteMember(Member muted, long time, TimeFrame timeFrame, String reason, @Nullable Member mod) {
        // Add role
        addMuteRole(muted);

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
                .addField("Moderator:", mod != null ? mod.getEffectiveName() : "<none>", false)
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
        Logger.info(" - Mod: <blue>" + (mod != null ? mod.getEffectiveName() : "<none>"));
    }

    public static void addMuteRole(Member muted) {
        Role role = BotHandler.getINSTANCE().getMutedRole();
        if (role != null) {
            muted.getGuild().addRoleToMember(muted, role).queue();
        }
    }

    public static void unMuteMember(Member muted, String reason, Member moderator) {
        boolean auto = reason == null;

        BotHandler.getINSTANCE().getMuteData().removeMute(muted.getId());

        // Remove role
        Role role = BotHandler.getINSTANCE().getMutedRole();
        if (role != null) {
            muted.getGuild().removeRoleFromMember(muted, role).queue();
        }

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
        member.getUser().openPrivateChannel().queue(s -> s.sendMessage(message).queue());
    }

    public static void banMessage(Member banned, String reason, Member moderator) {
        TextChannel botChannel = BotHandler.getINSTANCE().getBotChannel();
        String name = BotHandler.getBot().getSelfUser().getName();

        MessageEmbed embed = new EmbedBuilder()
                .setTitle("-- BAN TIME --")
                .setColor(Color.RED)
                .setAuthor(name, null, Util.IMAGE_URL)
                .addField("Banned:", banned.getEffectiveName() + "(" + banned.getId() + ")", false)
                .addField("Reason:", reason, false)
                .addField("Moderator:", moderator.getEffectiveName(), false)
                .build();

        botChannel.sendMessage(embed).queue();

        name = name + " (" + botChannel.getGuild().getName() + ")";
        MessageEmbed toUser = new EmbedBuilder(embed)
                .setAuthor(name, null, Util.IMAGE_URL)
                .build();
        MemberUtil.directMessage(banned, toUser);
    }

    private static long parseTimespan(long time, TimeFrame timeFrame) {

        return (time * timeFrame.getMilliseconds()) + System.currentTimeMillis();
    }

}
