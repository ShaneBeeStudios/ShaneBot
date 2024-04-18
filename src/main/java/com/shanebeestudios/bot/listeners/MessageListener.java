package com.shanebeestudios.bot.listeners;

import com.shanebeestudios.bot.BotHandler;
import com.shanebeestudios.bot.util.Logger;
import com.shanebeestudios.bot.util.MemberUtil;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class MessageListener extends ListenerAdapter {
    private final BotHandler botHandler;
    private final Map<Member, Integer> mentions = new HashMap<>();

    public MessageListener(BotHandler botHandler) {
        this.botHandler = botHandler;

        // Reset mentions every day
        new Timer("timer").schedule(new TimerTask() {
            @Override
            public void run() {
                mentions.clear();
            }
        }, 0, TimeUnit.DAYS.toMillis(1));
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        String ID = event.getGuild().getId();
        Message message = event.getMessage();
        TextChannel channel = event.getChannel().asTextChannel();

        // Prevent the bot running on an unauthorized guild
        if (!ID.equalsIgnoreCase(BotHandler.getInstance().getServerID())) {
            Logger.info("Attempting to use bot on guild: " + event.getGuild().getName());
            channel.sendMessage("**You are not authorized to use this bot!!!**").queue();
            return;
        }


        Member owner = event.getGuild().getOwner();

        // Shane no like getting tagged
        if (message.getMentions().getMembers().contains(owner)) {
            Member tagger = event.getMember();
            if (tagger == null) return;
            message.delete().queue();
            MemberUtil.mentionRemovalMessage(tagger, channel);
            addTagCount(tagger);
        }
    }

    private void addTagCount(Member member) {
        Logger.info("Attempting to log tag");
        int tag = 1;
        if (mentions.containsKey(member)) {
            tag += mentions.get(member);
            Logger.info("User already in mentions: tag: " + tag);
            if (tag > 2) {
                Member bot = (Member) this.botHandler.getBot().getSelfUser();
                //Member bot = botHandler.getBotChannel().getGuild().getMemberById(this.botHandler.getBot().getSelfUser().getId());
                MemberUtil.timeoutMember(member, 24, TimeUnit.HOURS, "Abusing mentions", bot);
                Logger.info("Do we make it this far?");
                mentions.remove(member);
                return;
            }
        }
        mentions.put(member, tag);
    }

}
