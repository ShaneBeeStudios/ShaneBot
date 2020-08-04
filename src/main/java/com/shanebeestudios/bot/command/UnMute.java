package com.shanebeestudios.bot.command;

import com.shanebeestudios.bot.util.MemberUtil;
import net.dv8tion.jda.api.entities.Member;

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

            MemberUtil.unMuteMember(muted, reason.toString(), member);
        } else {
            channel.sendMessage("**Invalid arguments:** !unmute <member(tag/id)> <reason>").queue();
        }
        return true;
    }

}
