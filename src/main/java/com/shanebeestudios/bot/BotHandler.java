package com.shanebeestudios.bot;

import com.shanebeestudios.bot.command.CommandActivity;
import com.shanebeestudios.bot.command.CommandBase;
import com.shanebeestudios.bot.command.CommandCommands;
import com.shanebeestudios.bot.command.CommandMute;
import com.shanebeestudios.bot.command.CommandPurge;
import com.shanebeestudios.bot.command.CommandRelease;
import com.shanebeestudios.bot.command.CommandSay;
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

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class BotHandler {

    private static BotHandler INSTANCE;
    private final String serverID, botChannelID, adminRoleID;
    private String botName;
    private final JDA bot;
    private final Guild guild;
    private final List<CommandBase> commands = new ArrayList<>();

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

        this.guild = this.bot.getGuildById(serverID);
        registerCommands();

        new ConsoleThread(this.bot.getSelfUser().getName()).start();

        Logger.info("Bot server loaded!");
    }

    private Object[] registerListeners() {
        List<ListenerAdapter> listeners = new ArrayList<>();
        listeners.add(new MessageListener(this));
        listeners.add(new JoinListener());
        return listeners.toArray(new ListenerAdapter[0]);
    }

    private void registerCommands() {
        commands.add(new CommandActivity(this, Permission.MESSAGE_MANAGE));
        commands.add(new CommandCommands(this, Permission.VIEW_CHANNEL));
        commands.add(new CommandMute(this, Permission.MODERATE_MEMBERS));
        commands.add(new CommandPurge(this, Permission.MESSAGE_MANAGE));
        commands.add(new CommandRelease(this, Permission.ADMINISTRATOR));
        commands.add(new CommandSay(this, Permission.MESSAGE_MANAGE));
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
     * Get the guild running this bot
     *
     * @return Guild running this bot
     */
    public Guild getGuild() {
        return this.guild;
    }

    /**
     * Get registered commands
     *
     * @return Registered commands
     */
    public List<CommandBase> getCommands() {
        return commands;
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
        this.bot.shutdownNow();
        try {
            this.bot.awaitShutdown();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
