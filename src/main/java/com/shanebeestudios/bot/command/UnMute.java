package com.shanebeestudios.bot.command;

import com.shanebeestudios.bot.BotHandler;
import com.shanebeestudios.bot.util.MemberUtil;
import com.shanebeestudios.bot.util.Util;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;

public class UnMute extends Command {

    public UnMute(boolean requiresAdmin) {
        super(requiresAdmin);
    }

    @Override
    public boolean run() {
        if (args.length >= 2) {
            Member muted = parseMember(0);
            if (muted == null) {
                channel.sendMessage("Invalid Member: " + args[0]).queue();
                return true;
            }

            StringBuilder reason = new StringBuilder();
            for (int i = 1; i < args.length; i++) {
                reason.append(args[i]).append(" ");
            }

            MemberUtil.removeMutedRole(muted);
            unmuteMessage(muted, reason.toString());
        }
        return true;
    }

    private void unmuteMessage(Member muted, String reason) {
        TextChannel botChannel = BotHandler.getINSTANCE().getBotChannel();
        String name = BotHandler.getBot().getSelfUser().getName();

        MessageEmbed embed = new EmbedBuilder()
                .setTitle("-- UN-MUTE --")
                .setColor(Color.GREEN)
                .setAuthor(name, null, Util.IMAGE_URL)
                .addField("UnMuted:", muted.getEffectiveName() + "(" + muted.getId() + ")", false)
                .addField("Reason:", reason, false)
                .addField("Moderator:", member.getEffectiveName(), false)
                .build();

        botChannel.sendMessage(embed).queue();

        name = name + " (" + botChannel.getGuild().getName() + ")";
        MessageEmbed toUser = new EmbedBuilder(embed)
                .setAuthor(name, null, Util.IMAGE_URL)
                .build();
        MemberUtil.directMessage(muted, toUser);
    }

}
