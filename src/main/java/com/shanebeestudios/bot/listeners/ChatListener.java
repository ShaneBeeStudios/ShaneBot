package com.shanebeestudios.bot.listeners;

import com.shanebeestudios.bot.BotHandler;
import com.shanebeestudios.bot.util.MemberUtil;
import com.shanebeestudios.bot.util.TimeFrame;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class ChatListener extends ListenerAdapter {

    private final BotHandler botHandler;
    private final Map<Member, Integer> mentions = new HashMap<>();

    public ChatListener(BotHandler botHandler) {
        this.botHandler = botHandler;
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        Message message = event.getMessage();
        Member owner = event.getGuild().getOwner();

        // Shane no like getting tagged
        if (message.getMentionedMembers().contains(owner)) {
            Member tagger = event.getMember();
            if (tagger == null) return;
            MemberUtil.mentionRemovalMessage(tagger, event.getTextChannel());
            event.getMessage().delete().queue();
            addTagCount(tagger);
        }
    }

    private void addTagCount(Member member) {
        int tag = 1;
        if (mentions.containsKey(member)) {
            tag += mentions.get(member);
            if (tag > 2) {
                Member bot = botHandler.getBotChannel().getGuild().getMemberById(BotHandler.getBot().getSelfUser().getId());
                MemberUtil.muteMember(member, 24, TimeFrame.HOUR, "Abusing mentions", bot);
                mentions.remove(member);
                return;
            }
        }
        mentions.put(member, tag);
    }

}
