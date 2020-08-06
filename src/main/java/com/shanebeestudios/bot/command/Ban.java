package com.shanebeestudios.bot.command;

import com.shanebeestudios.bot.util.MemberUtil;

public class Ban extends Command {

    public Ban(Permission permission) {
        super(permission);
        this.description = "Ban a member from this guild";
        this.usage = "!ban <member(tag/id)> <reason>";
    }

    @Override
    public boolean run() {
        if (args.length >= 2) {
            parseMember(0, banned -> {
                StringBuilder reason = new StringBuilder();
                for (int i = 1; i < args.length; i++) {
                    reason.append(args[i]).append(" ");
                }
                MemberUtil.banMessage(banned, reason.toString(), member);
                channel.getGuild().ban(banned, 1, reason.toString()).queue();
            });
        }
        return true;
    }

}
