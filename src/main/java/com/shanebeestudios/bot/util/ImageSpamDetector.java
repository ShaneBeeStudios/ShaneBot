package com.shanebeestudios.bot.util;

import net.dv8tion.jda.api.entities.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Detects image-spam scam behaviour: a user posting images in many channels in quick succession.
 * <p>Threshold: {@value CHANNEL_THRESHOLD}+ distinct channels within {@value TIME_WINDOW_MS}ms.</p>
 */
public class ImageSpamDetector {

    /** How many distinct channels within the window triggers a flag. */
    private static final int CHANNEL_THRESHOLD = 3;

    /** Rolling window in milliseconds. */
    private static final long TIME_WINDOW_MS = 10_000L;

    /** One tracked post: the channel it was in, when it arrived, and the message itself for deletion. */
    private record SpamEntry(long channelId, long timestamp, Message message) {}

    /** userId -> list of recent image posts. */
    private final ConcurrentHashMap<Long, List<SpamEntry>> tracking = new ConcurrentHashMap<>();

    /**
     * Record an image-containing message and check whether this user has crossed the spam threshold.
     *
     * @param message The message that contains at least one image attachment or image embed.
     * @return A list of all tracked messages to delete + act on, or {@code null} if the threshold
     *         has not been crossed yet.
     */
    public synchronized List<Message> recordAndCheck(Message message) {
        long userId = message.getAuthor().getIdLong();
        long channelId = message.getChannel().getIdLong();
        long now = System.currentTimeMillis();

        List<SpamEntry> entries = tracking.computeIfAbsent(userId, k -> new ArrayList<>());

        // Drop stale entries outside the rolling window
        entries.removeIf(e -> now - e.timestamp() > TIME_WINDOW_MS);

        // Only record one entry per channel per window — no inflating the count by spamming the same channel
        boolean channelAlreadyTracked = entries.stream().anyMatch(e -> e.channelId() == channelId);
        if (!channelAlreadyTracked) {
            entries.add(new SpamEntry(channelId, now, message));
        }

        // Count distinct channels in the current window
        long distinctChannels = entries.stream()
                .mapToLong(SpamEntry::channelId)
                .distinct()
                .count();

        if (distinctChannels >= CHANNEL_THRESHOLD) {
            // Collect all tracked messages, clear state, and signal the caller to act
            List<Message> flaggedMessages = entries.stream()
                    .map(SpamEntry::message)
                    .toList();
            tracking.remove(userId);
            return flaggedMessages;
        }

        return null;
    }

    /**
     * Clear all tracking data for a user (e.g. after they have been actioned or on a daily reset).
     *
     * @param userId The user's Discord ID.
     */
    public synchronized void clear(long userId) {
        tracking.remove(userId);
    }

}
