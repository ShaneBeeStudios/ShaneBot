package com.shanebeestudios.bot.command;

import com.shanebeestudios.bot.BotHandler;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.requests.restaction.CommandCreateAction;

import java.util.Locale;
import java.util.function.Function;

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

    protected void createCommand() {
        createCommand(commandCreateAction -> commandCreateAction);
    }

    protected void createCommand(Function<CommandCreateAction, CommandCreateAction> command) {
        command.apply(botHandler.getGuild().upsertCommand(getCommandName(), getDescription())
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(getPermission())))
            .queue();
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

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (!event.getName().equalsIgnoreCase(getName())) return;
        onCommand(event);
    }

    abstract void onCommand(SlashCommandInteractionEvent event);
}
