package com.shanebeestudios.bot.command;

import com.shanebeestudios.bot.BotHandler;
import com.shanebeestudios.bot.util.Logger;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import java.util.List;

public class CommandPurge extends CommandBase {

    public CommandPurge(BotHandler botHandler, Permission permission) {
        super(botHandler, permission, "Purge", "Purge messages in a channel");
        createCommand(command -> command
            .addOption(OptionType.INTEGER, "amount", "Amount of messages of messages to purge.", true));
    }

    @SuppressWarnings("DataFlowIssue")
    @Override
    public void onCommand(SlashCommandInteractionEvent event) {
        event.deferReply().setEphemeral(true).complete();

        int amount = event.getOption("amount").getAsInt();
        MessageChannelUnion channel = event.getChannel();
        List<Message> messages = channel.getHistory().retrievePast(amount).complete();
        channel.purgeMessages(messages);

        amount = messages.size();
        Logger.info("Purged <cyan>" + amount + "<reset> messages in <purple>" + channel.getName());
        event.getHook().editOriginal("Purged **" + amount + "** messages").queue();
    }

}
