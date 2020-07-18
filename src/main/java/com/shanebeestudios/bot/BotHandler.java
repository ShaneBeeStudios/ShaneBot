package com.shanebeestudios.bot;

import com.shanebeestudios.bot.command.Command;
import com.shanebeestudios.bot.command.Ban;
import com.shanebeestudios.bot.command.Mute;
import com.shanebeestudios.bot.command.Purge;
import com.shanebeestudios.bot.command.Test;
import com.shanebeestudios.bot.command.UnMute;
import com.shanebeestudios.bot.data.MuteData;
import com.shanebeestudios.bot.listeners.CommandListener;
import com.shanebeestudios.bot.listeners.JoinListener;
import com.shanebeestudios.bot.task.MuteTimer;
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
    private final Map<String, Command> commands = new HashMap<>();
    private final String server, welcome_c, rules_c, bot_c, muted_r, admin_r;
    private final MuteData muteData;

    // Channels
    private TextChannel WELCOME_CHANNEL;
    private TextChannel RULES_CHANNEL;
    private TextChannel BOT_CHANNEL;

    // Rules
    private Role MUTED_ROLE;
    private Role ADMIN_ROLE;

    BotHandler(String token, String server, String welcome_c, String rules_c, String bot_c, String muted_r, String admin_r) {
        INSTANCE = this;
        this.server = server;
        this.welcome_c = welcome_c;
        this.rules_c = rules_c;
        this.bot_c = bot_c;
        this.muted_r = muted_r;
        this.admin_r = admin_r;

        try {
            bot = JDABuilder
                    .createDefault(token)
                    .addEventListeners(new CommandListener(this.commands), new JoinListener())
                    .build();
        } catch (LoginException e) {
            e.printStackTrace();
        }
        registerCommands();
        this.muteData = new MuteData();
        new MuteTimer(this, 1);
    }

    private void registerCommands() {
        commands.put("purge", new Purge(true));
        commands.put("mute", new Mute(true));
        commands.put("unmute", new UnMute(true));
        commands.put("ban", new Ban(true));
        commands.put("test", new Test(true));
    }

    public static JDA getBot() {
        return bot;
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

    public Role getMutedRole() {
        if (MUTED_ROLE == null) {
            MUTED_ROLE = bot.getRoleById(muted_r);
        }
        return MUTED_ROLE;
    }

    public Role getAdminRole() {
        if (ADMIN_ROLE == null) {
            ADMIN_ROLE = bot.getRoleById(admin_r);
        }
        return ADMIN_ROLE;
    }

    public MuteData getMuteData() {
        return muteData;
    }

    public static BotHandler getINSTANCE() {
        return INSTANCE;
    }

}
