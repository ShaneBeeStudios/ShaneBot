package com.shanebeestudios.bot.command;

import com.shanebeestudios.bot.BotHandler;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import java.util.EnumSet;
import java.util.List;

public class CommandPurge extends CommandBase {

    public CommandPurge(BotHandler botHandler, Permission permission) {
        super(botHandler, permission, "Purge", "Purge messages in a channel");
        botHandler.getGuild().upsertCommand(getCommandName(), getDescription())
                .addOption(OptionType.INTEGER, "amount", "amount of messages", true)
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(getPermission()))
                .queue();
    }

    @SuppressWarnings("DataFlowIssue")
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (!event.getName().equals("purge")) return;
        event.deferReply().complete();

        int amount = event.getOption("amount").getAsInt();
        MessageChannelUnion channel = event.getChannel();
        List<Message> messages = channel.getHistory().retrievePast(amount + 1).complete();
        channel.purgeMessages(messages);

        event.getHook().deleteOriginal().queue();
    }

}
