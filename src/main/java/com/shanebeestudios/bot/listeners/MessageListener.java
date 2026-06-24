package com.shanebeestudios.bot.listeners;

import com.shanebeestudios.bot.BotHandler;
import com.shanebeestudios.bot.util.ImageSpamDetector;
import com.shanebeestudios.bot.util.Logger;
import com.shanebeestudios.bot.util.MemberUtil;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.utils.FileUpload;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class MessageListener extends ListenerAdapter {
    private final BotHandler botHandler;
    private final Map<Member, Integer> mentions = new HashMap<>();
    private final ImageSpamDetector imageSpamDetector = new ImageSpamDetector();

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

        Member member = event.getMember();
        if (member == null) return;

        // --- Image spam / scam detection ---
        boolean hasImageAttachment = message.getAttachments().stream().anyMatch(Message.Attachment::isImage);
        if (hasImageAttachment) {
            List<Message> flagged = imageSpamDetector.recordAndCheck(message);
            if (flagged != null) {
                // Download unique images before deleting — we re-upload them to the bot channel
                // so they remain visible for admin review regardless of message deletion
                List<FileUpload> imageUploads = new ArrayList<>();
                Set<String> seenFilenames = new HashSet<>();
                for (Message spamMsg : flagged) {
                    for (Message.Attachment attachment : spamMsg.getAttachments()) {
                        if (!attachment.isImage()) continue;
                        // Use filename deduplication since scammers post the same image everywhere
                        if (!seenFilenames.add(attachment.getFileName())) continue;
                        try {
                            InputStream stream = attachment.getProxy().download().get();
                            imageUploads.add(FileUpload.fromData(stream, attachment.getFileName()));
                        } catch (Exception e) {
                            Logger.warn("Could not download spam image: " + e.getMessage());
                        }
                    }
                }

                // Delete all the tracked spam messages
                for (Message spamMsg : flagged) {
                    spamMsg.delete().queue(
                            success -> {},
                            err -> Logger.warn("Could not delete spam message: " + err.getMessage())
                    );
                }

                // Timeout the member and alert admins (with the downloaded images attached)
                MemberUtil.scamSpamDetected(member, flagged.size(), imageUploads);
                return;
            }
        }

        Member owner = event.getGuild().getOwner();

        // Shane no like getting tagged
        if (message.getMentions().getMembers().contains(owner)) {
            message.delete().queue();
            MemberUtil.mentionRemovalMessage(member, channel);
            addTagCount(member);
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
                MemberUtil.timeoutMember(member, 24, TimeUnit.HOURS, "Abusing mentions", bot);
                Logger.info("Do we make it this far?");
                mentions.remove(member);
                return;
            }
        }
        mentions.put(member, tag);
    }

}
