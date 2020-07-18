package com.shanebeestudios.bot;

import com.shanebeestudios.bot.command.Command;
import com.shanebeestudios.bot.command.Ban;
import com.shanebeestudios.bot.command.Mute;
import com.shanebeestudios.bot.command.Purge;
import com.shanebeestudios.bot.command.Test;
import com.shanebeestudios.bot.command.UnMute;
import com.shanebeestudios.bot.listeners.CommandListener;
import com.shanebeestudios.bot.listeners.JoinListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;

import javax.security.auth.login.LoginException;
import java.util.HashMap;
import java.util.Map;

public class BotHandler {

    private static BotHandler INSTANCE;
    private static JDA bot;
    private final Map<String, Command> commands = new HashMap<String, Command>();
    private final String server, welcome_c, rules_c, bot_c, muted_c;

    // Channels
    private TextChannel WELCOME_CHANNEL;
    private TextChannel RULES_CHANNEL;
    private TextChannel BOT_CHANNEL;

    // Rules
    private Role MUTED_ROLE;

    BotHandler(String token, String server, String welcome_c, String rules_c, String bot_c, String muted_c) {
        INSTANCE = this;
        this.server = server;
        this.welcome_c = welcome_c;
        this.rules_c = rules_c;
        this.bot_c = bot_c;
        this.muted_c = muted_c;

        try {
            bot = JDABuilder
                    .createDefault(token)
                    .addEventListeners(new CommandListener(this.commands), new JoinListener())
                    .build();
        } catch (LoginException e) {
            e.printStackTrace();
        }
        registerCommands();
    }

    private void registerCommands() {
        commands.put("purge", new Purge());
        commands.put("mute", new Mute());
        commands.put("unmute", new UnMute());
        commands.put("ban", new Ban());
        commands.put("test", new Test());
    }

    public static JDA getBot() {
        return bot;
    }

    public Role getMutedRole() {
        if (MUTED_ROLE == null) {
            MUTED_ROLE = bot.getRoleById(muted_c);
        }
        return MUTED_ROLE;
    }

    public TextChannel getBotChannel() {
        if (BOT_CHANNEL == null) {
            BOT_CHANNEL = bot.getTextChannelById(bot_c);
        }
        return BOT_CHANNEL;
    }

    public TextChannel getWelcomeChannel() {
        if (WELCOME_CHANNEL == null) {
            WELCOME_CHANNEL = bot.getTextChannelById(welcome_c);
        }
        return WELCOME_CHANNEL;
    }

    public TextChannel getRulesChannel() {
        if (RULES_CHANNEL == null) {
            RULES_CHANNEL = bot.getTextChannelById(rules_c);
        }
        return RULES_CHANNEL;
    }

    public static BotHandler getINSTANCE() {
        return INSTANCE;
    }

}
