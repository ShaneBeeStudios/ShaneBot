package com.shanebeestudios.bot.listeners;

import com.shanebeestudios.bot.command.Command;
import com.shanebeestudios.bot.util.Logger;
import com.shanebeestudios.bot.util.Util;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class CommandListener extends ListenerAdapter {

    private final Map<String, Command> commandMap;

    public CommandListener(Map<String, Command> commandMap) {
        this.commandMap = commandMap;
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        String message = event.getMessage().getContentRaw();
        Member member = event.getMember();

        if (message.charAt(0) == '!') {
            String fullCommand = message.substring(1);
            String[] splitCommand = fullCommand.split(" ");
            String command = splitCommand[0];

            if (commandMap.containsKey(command)) {
                Command baseCommand = commandMap.get(command);
                String[] args = Util.getSliceOfArray(splitCommand, 1, splitCommand.length);
                if (baseCommand.run(event, args)) {
                    assert member != null;
                    Logger.info(member.getEffectiveName() + " issued command: <blue>" + fullCommand);
                }
            }

        }
    }

}
