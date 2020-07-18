package com.shanebeestudios.bot.command;

import com.shanebeestudios.bot.BotHandler;
import net.dv8tion.jda.api.entities.TextChannel;

public class Test extends Command {

    @Override
    public boolean run() {
        TextChannel welcomeChannel = BotHandler.getINSTANCE().getBotChannel(); // TEMP for testing
        TextChannel rulesChannel = BotHandler.getINSTANCE().getBotChannel(); // TEMP for testing
        welcomeChannel.sendMessage("Welcome " + member.getAsMention() + " to **" + message.getGuild().getName() + "**, Please " +
                "make sure to read over " + rulesChannel.getAsMention() +
                " ... not knowing the rules for this Discord guild will not be an excuse when a rule is broken!").queue();
        return true;
    }

}
