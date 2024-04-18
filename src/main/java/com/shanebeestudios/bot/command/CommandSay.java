package com.shanebeestudios.bot.command;

import com.shanebeestudios.bot.BotHandler;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;

public class CommandSay extends CommandBase {

    public CommandSay(BotHandler botHandler, Permission permission) {
        super(botHandler, permission, "Say", "Make the bot send a message");
        createCommand(command -> command
            .addOption(OptionType.STRING, "message", "Message to send", true));
    }

    @SuppressWarnings("DataFlowIssue")
    @Override
    public void onCommand(SlashCommandInteractionEvent event) {
        event.deferReply().setEphemeral(true).queue();
        String message = event.getOption("message").getAsString();
        event.getChannel().sendMessage(message).complete();
        event.getHook().deleteOriginal().queue();
    }

}
