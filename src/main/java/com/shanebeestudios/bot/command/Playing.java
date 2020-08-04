package com.shanebeestudios.bot.command;

import com.shanebeestudios.bot.BotHandler;
import net.dv8tion.jda.api.entities.Activity;

public class Playing extends Command {

    public Playing(boolean requiresAdmin) {
        super(requiresAdmin);
    }

    @Override
    public boolean run() {
        if (args.length > 0) {
            StringBuilder reason = new StringBuilder();
            for (String arg : args) {
                reason.append(arg).append(" ");
            }
            BotHandler.getBot().getPresence().setActivity(Activity.playing(reason.toString()));
        } else {
            channel.sendMessage("**Invalid Arguments:** !play <what to play>").queue();
        }
        message.delete().queue();
        return true;
    }
}
