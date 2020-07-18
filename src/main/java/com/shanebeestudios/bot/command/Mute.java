package com.shanebeestudios.bot.command;

import com.shanebeestudios.bot.BotHandler;
import com.shanebeestudios.bot.util.MemberUtil;
import com.shanebeestudios.bot.util.TimeFrame;
import com.shanebeestudios.bot.util.Util;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;

public class Mute extends Command {

    @Override
    public boolean run() {
        if (args.length >= 4) {
            Member muted = parseMember(0);
            if (muted == null) {
                channel.sendMessage("Invalid Member: " + args[0]).queue();
                return true;
            }

            int t = Util.parseInt(args[1]);
            if (t == 0) {
                channel.sendMessage("Invalid time value: " + args[1]).queue();
                return true;
            }

            TimeFrame timeFrame = TimeFrame.match(args[2]);
            if (timeFrame == null) {
                channel.sendMessage("Invalid time frame: " + args[2]).queue();
                return true;
            }

            StringBuilder reason = new StringBuilder();
            for (int i = 3; i < args.length; i++) {
                reason.append(args[i]).append(" ");
            }

            MemberUtil.addMutedRole(muted);
            muteMessage(muted, t + " " + timeFrame.getName() + (t > 1 ? "s" : ""), reason.toString());
        }
        return true;
    }

    private void muteMessage(Member muted, String time, String reason) {
        TextChannel botChannel = BotHandler.getINSTANCE().getBotChannel();
        String name = BotHandler.getBot().getSelfUser().getName();

        MessageEmbed embed = new EmbedBuilder()
                .setTitle("-- MUTE TIME --")
                .setColor(Color.ORANGE)
                .setAuthor(name, null, "https://i.imgur.com/VbV0p7j.png")
                .addField("Muted:", muted.getEffectiveName() + "(" + muted.getId() + ")", false)
                .addField("Time:", time, false)
                .addField("Reason:", reason, false)
                .addField("Moderator:", member.getEffectiveName(), false)
                .build();

        botChannel.sendMessage(embed).queue();

        name = name + " (" + botChannel.getGuild().getName() + ")";
        MessageEmbed toUser = new EmbedBuilder(embed)
                .setAuthor(name, null, "https://i.imgur.com/VbV0p7j.png")
                .build();
        MemberUtil.directMessage(muted, toUser);
    }

    private long parseTimespan(String number, TimeFrame timeFrame) {
        long i = Util.parseInt(number);
        if (i == 0) return 0;

        return timeFrame.getMilliseconds() + System.currentTimeMillis();
    }


}
