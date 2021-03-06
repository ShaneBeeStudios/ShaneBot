package com.shanebeestudios.bot.command;

import com.shanebeestudios.bot.util.MemberUtil;
import com.shanebeestudios.bot.util.TimeFrame;
import com.shanebeestudios.bot.util.Util;

public class Mute extends Command {

    public Mute(Permission permission) {
        super(permission);
        this.description = "Mute a member for a specific time";
        this.usage = "!mute <member(tag/id)> <time> <reason>";
    }

    @Override
    public boolean run() {
        message.delete().queue();
        if (args.length >= 4) {
            parseMember(0, muted -> {
                int t = Util.parseInt(args[1]);
                if (t == 0) {
                    channel.sendMessage("Invalid time value: " + args[1]).queue();
                    return;
                }

                TimeFrame timeFrame = TimeFrame.match(args[2]);
                if (timeFrame == null) {
                    channel.sendMessage("Invalid time frame: " + args[2]).queue();
                    return;
                }

                StringBuilder reason = new StringBuilder();
                for (int i = 3; i < args.length; i++) {
                    reason.append(args[i]).append(" ");
                }

                MemberUtil.muteMember(muted, t, timeFrame, reason.toString(), member);
            });
        } else {
            channel.sendMessage("**Invalid Arguments:** !mute <member(tag/id)> <time> <reason>").queue();
        }
        return true;
    }

}
