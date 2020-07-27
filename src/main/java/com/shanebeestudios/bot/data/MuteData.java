package com.shanebeestudios.bot.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.shanebeestudios.bot.BotHandler;
import com.shanebeestudios.bot.util.Logger;
import com.shanebeestudios.bot.util.MemberUtil;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("deprecation")
public class MuteData {

    private final Map<String, Long> mutes = new HashMap<>();
    private final Path FILE_PATH = Paths.get(".", "data");
    private File MUTE_DATA_FILE;

    public MuteData() {
        loadMuteDataFile();
    }

    public void addMute(Member member, long time) {
        mutes.put(member.getId(), time);
        writeFile(true);
    }

    public void removeMute(String member) {
        mutes.remove(member);
        writeFile(true);
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

        TextChannel channel = BotHandler.getINSTANCE().getBotChannel();
        if (channel == null) return;
        Guild guild = channel.getGuild();
        for (String muted : toUnMute) {
            removeMute(muted);
            guild.retrieveMemberById(muted).queue( member -> {
                if (member != null) {
                    MemberUtil.unMuteMember(member, null, null);
                }
            });
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void loadMuteDataFile() {
        MUTE_DATA_FILE = new File(FILE_PATH + "/" + "mutes.json");
        if (Files.notExists(FILE_PATH)) {
            try {
                Files.createDirectory(FILE_PATH);
            } catch (IOException ignore) {
            }
        }

        if (!MUTE_DATA_FILE.exists()) {
            try {
                MUTE_DATA_FILE.createNewFile();

            } catch (IOException ignore) {
            }
        } else {
            readFile(false);
        }
    }

    private void writeFile(boolean silent) {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .create();
        try {
            if (!silent)
                Logger.info("Writing mutes to file...");

            Writer writer = Files.newBufferedWriter(MUTE_DATA_FILE.toPath());
            gson.toJson(this.mutes, writer);
            writer.close();

            if (!silent)
                Logger.info("Successfully written mutes to file!");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void readFile(boolean silent) {
        Gson gson = new Gson();

        try {
            if (!silent)
                Logger.info("Loading mutes from file...");

            Reader reader = Files.newBufferedReader(MUTE_DATA_FILE.toPath());

            Map<?, ?> mutes = gson.fromJson(reader, Map.class);

            if (mutes != null) {
                for (Map.Entry<?, ?> entry : mutes.entrySet()) {
                    String name = ((String) entry.getKey());
                    long time = new Double((double) entry.getValue()).longValue();
                    this.mutes.put(name, time);
                }
            }

            if (!silent)
                Logger.info("Successfully loaded " + this.mutes.size() + " mutes from file!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveAll() {
        writeFile(false);
    }

}
