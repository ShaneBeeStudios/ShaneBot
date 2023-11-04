package com.shanebeestudios.bot.command;

import com.shanebeestudios.bot.BotHandler;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Locale;

public abstract class CommandBase extends ListenerAdapter {

    protected final BotHandler botHandler;
    private final Permission permission;
    private final String name;
    private final String description;

    public CommandBase(BotHandler botHandler, Permission permission, String name, String description) {
        this.botHandler = botHandler;
        this.permission = permission;
        this.name = name;
        this.description = description;
        botHandler.getBot().addEventListener(this);
    }

    public Permission getPermission() {
        return permission;
    }

    public String getName() {
        return name;
    }
    
    protected String getCommandName() {
        return this.name.toLowerCase(Locale.ROOT);
    }

    public String getDescription() {
        return description;
    }

}
