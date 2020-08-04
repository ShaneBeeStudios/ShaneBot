package com.shanebeestudios.bot.command;

import com.shanebeestudios.bot.util.MemberUtil;
import net.dv8tion.jda.api.entities.Member;

public class Ban extends Command {

    public Ban(boolean requiresAdmin) {
        super(requiresAdmin);
    }

    @Override
    public boolean run() {
        if (args.length >= 2) {
            parseMember(0, banned -> {
                if (banned == null) {
                    channel.sendMessage("Invalid Member: " + args[0]).queue();
                    return;
                }

                StringBuilder reason = new StringBuilder();
                for (int i = 1; i < args.length; i++) {
                    reason.append(args[i]).append(" ");
                }
                MemberUtil.banMessage(banned, reason.toString(), member);
                channel.getGuild().ban(banned, 1, reason.toString()).queue(); //TODO will test first before actually banning anyone
            });
        }
        return true;
    }

}
