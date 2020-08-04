package com.shanebeestudios.bot.command;

import com.shanebeestudios.bot.util.MemberUtil;
import com.shanebeestudios.bot.util.TimeFrame;
import com.shanebeestudios.bot.util.Util;
import net.dv8tion.jda.api.entities.Member;

public class Mute extends Command {

    public Mute(boolean requiresAdmin) {
        super(requiresAdmin);
    }

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

            MemberUtil.muteMember(muted, t, timeFrame, reason.toString(), member);
        } else {
            channel.sendMessage("**Invalid Arguments:** !mute <member(tag/id)> <time> <reason>").queue();
        }
        return true;
    }

}
