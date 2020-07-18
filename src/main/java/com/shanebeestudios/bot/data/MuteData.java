package com.shanebeestudios.bot.data;

import com.shanebeestudios.bot.BotHandler;
import com.shanebeestudios.bot.util.Logger;
import com.shanebeestudios.bot.util.MemberUtil;
import net.dv8tion.jda.api.entities.Member;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MuteData {

    private final Map<String, Long> mutes = new HashMap<>();

    public MuteData() {
        // will be putting file stuff here, or something
    }

    public void addMute(Member member, long time) {
        mutes.put(member.getId(), time);
    }

    public void removeMute(String member) {
        mutes.remove(member);
    }

    public boolean isMuted(Member member) {
        return mutes.containsKey(member.getId());
    }

    public void checkMutes() {
        long time = System.currentTimeMillis();
        List<String> toUnMute = new ArrayList<>();

        for (String muted : mutes.keySet()) {
            if (mutes.get(muted) < time) {
                toUnMute.add(muted);
            }
        }

        for (String muted : toUnMute) {
            Member m = BotHandler.getINSTANCE().getWelcomeChannel().getGuild().getMemberById(muted);
            if (m != null) {
                MemberUtil.unMuteMember(m, null, null);
            } else {
                Logger.warn("Member not found: " + muted);
            }
        }
    }

}
