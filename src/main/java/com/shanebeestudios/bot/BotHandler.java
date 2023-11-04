package com.shanebeestudios.bot;

import com.shanebeestudios.bot.command.Playing;
import com.shanebeestudios.bot.command.Purge;
import com.shanebeestudios.bot.command.Release;
import com.shanebeestudios.bot.command.Say;
import com.shanebeestudios.bot.listeners.JoinListener;
import com.shanebeestudios.bot.listeners.MessageListener;
import com.shanebeestudios.bot.task.ConsoleThread;
import com.shanebeestudios.bot.util.Logger;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class BotHandler {

    private static BotHandler INSTANCE;
    private static JDA bot;
    private static String BOT_NAME = "Bot Loading...";
    private final String server, bot_c, admin_r;

    // Channels
    private TextChannel botChannel;

    // Rules
    private Role adminRole;

    BotHandler(String token, String server, String bot_c, String admin_r) {
        INSTANCE = this;
        Logger.info("Starting server...");
        this.server = server;
        this.bot_c = bot_c;
        this.admin_r = admin_r;

        Logger.info("Logging in bot");
        try {
            bot = JDABuilder
                    .createDefault(token)
                    .enableIntents(EnumSet.allOf(GatewayIntent.class))
                    .addEventListeners(new JoinListener())
                    .addEventListeners(registerListeners())
                    .build().awaitReady();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        BOT_NAME = bot.getSelfUser().getName();
        Logger.info("Successfully logged in bot: <blue>" + BOT_NAME);

        Guild guild = bot.getGuildById(server);
        if (guild != null) registerCommands(guild);


        new ConsoleThread(bot.getSelfUser().getName()).start();

        Logger.info("Bot server loaded!");
    }

    private Object[] registerListeners() {
        List<ListenerAdapter> listeners = new ArrayList<>();
        listeners.add(new Purge());
        listeners.add(new Say());
        listeners.add(new Release());
        listeners.add(new Playing());
        listeners.add(new MessageListener(this));
        return listeners.toArray(new ListenerAdapter[0]);
    }

    public void registerCommands(@NotNull Guild guild) {
        Playing.registerCommand(guild);
        Purge.registerCommand(guild);
        Release.registerCommand(guild);
        Say.registerCommand(guild);
    }

    public static JDA getBot() {
        return bot;
    }

    public static String getBotName() {
        return BOT_NAME;
    }

    public TextChannel getBotChannel() {
        if (botChannel == null) {
            botChannel = bot.getTextChannelById(bot_c);
        }
        return botChannel;
    }

    public Role getAdminRole() {
        if (adminRole == null) {
            adminRole = bot.getRoleById(admin_r);
        }
        return adminRole;
    }

    public String getServerID() {
        return server;
    }

    public static BotHandler getInstance() {
        return INSTANCE;
    }

}
