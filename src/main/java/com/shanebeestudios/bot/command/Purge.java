package com.shanebeestudios.bot.command;

import com.shanebeestudios.bot.util.Util;
import net.dv8tion.jda.api.entities.Message;

import java.util.List;

public class Purge extends Command {

    @Override
    public boolean run() {
        if (args.length == 0) {
            channel.sendMessage("You need to specify a number!").queue();
        } else {
            int amount = Util.getInt(args[0]);
            List<Message> messages = channel
                    .getHistory()
                    .retrievePast(amount + 1)
                    .complete();
            if (messages.size() > 1) {
                channel.deleteMessages(messages).complete();
            }
        }
        return true;
    }

}
