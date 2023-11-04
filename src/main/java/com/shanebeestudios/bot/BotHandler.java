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
import net.dv8tion.jda.api.Permission;
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
    private final String serverID, botChannelID, adminRoleID;
    private String botName;
    private final JDA bot;

    // Channels
    private TextChannel botChannel;

    // Roles
    private Role adminRole;

    BotHandler(String token, String serverID, String botChannelID, String adminRoleID) {
        INSTANCE = this;
        this.botName = "Bot Loading";
        Logger.info("Starting server...");
        this.serverID = serverID;
        this.botChannelID = botChannelID;
        this.adminRoleID = adminRoleID;

        Logger.info("Logging in bot");
        try {
            this.bot = JDABuilder.createDefault(token)
                    .enableIntents(EnumSet.allOf(GatewayIntent.class))
                    .addEventListeners(registerListeners())
                    .build().awaitReady();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        this.botName = this.bot.getSelfUser().getName();
        Logger.info("Successfully logged in bot: <blue>" + this.botName);

        Guild guild = this.bot.getGuildById(serverID);
        if (guild != null) registerCommands(guild);

        new ConsoleThread(this.bot.getSelfUser().getName()).start();

        Logger.info("Bot server loaded!");
    }

    private Object[] registerListeners() {
        List<ListenerAdapter> listeners = new ArrayList<>();
        listeners.add(new Purge());
        listeners.add(new Say());
        listeners.add(new Release(this));
        listeners.add(new Playing(this));
        listeners.add(new MessageListener(this));
        listeners.add(new JoinListener());
        return listeners.toArray(new ListenerAdapter[0]);
    }

    private void registerCommands(@NotNull Guild guild) {
        EnumSet<Permission> permissions = getAdminRole().getPermissions();
        Playing.registerCommand(guild, permissions);
        Purge.registerCommand(guild, permissions);
        Release.registerCommand(guild, permissions);
        Say.registerCommand(guild, permissions);
    }

    /**
     * Get instance of the bot
     *
     * @return Instance of bot
     */
    public JDA getBot() {
        return this.bot;
    }

    /**
     * Get name of bot
     *
     * @return Name of bot
     */
    public String getBotName() {
        return this.botName;
    }

    /**
     * Get bot channel
     * <p>This is the channel where the bot will log messages</p>
     *
     * @return Bot channel
     */
    public TextChannel getBotChannel() {
        if (this.botChannel == null) {
            this.botChannel = this.bot.getTextChannelById(this.botChannelID);
        }
        return this.botChannel;
    }

    /**
     * Get admin role
     *
     * @return Admin role
     */
    public Role getAdminRole() {
        if (this.adminRole == null) {
            this.adminRole = this.bot.getRoleById(this.adminRoleID);
        }
        return this.adminRole;
    }

    /**
     * Get server ID
     *
     * @return Server ID
     */
    public String getServerID() {
        return this.serverID;
    }

    /**
     * Get instance of BotHandler
     *
     * @return Instance of BotHandler
     */
    public static BotHandler getInstance() {
        return INSTANCE;
    }

    /**
     * Shutdown the bot
     */
    public void shutdown() {
        this.bot.shutdown();
        try {
            this.bot.awaitShutdown();
            Logger.info("Successfully shutdown bot!");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
