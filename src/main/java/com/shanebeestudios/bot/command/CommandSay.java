package com.shanebeestudios.bot.command;

import com.shanebeestudios.bot.BotHandler;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import java.util.EnumSet;

public class CommandSay extends ListenerAdapter {

    public CommandSay(BotHandler botHandler, EnumSet<Permission> permissions) {
        botHandler.getBot().addEventListener(this);
        botHandler.getGuild().upsertCommand("say", "Send a message from the bot")
                .addOption(OptionType.STRING, "message", "Message to send", true)
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(permissions))
                .queue();
    }

    @SuppressWarnings("DataFlowIssue")
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (!event.getName().equalsIgnoreCase("say")) return;
        event.deferReply().queue();
        String message = event.getOption("message").getAsString();
        event.getChannel().sendMessage(message).complete();
        event.getHook().deleteOriginal().queue();
    }

}
