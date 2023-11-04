package com.shanebeestudios.bot.command;

import com.shanebeestudios.bot.BotHandler;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Playing extends ListenerAdapter {

//    public Playing(Permission permission) {
//        super(permission);
//        this.description = "Set the 'game' the bot is playing";
//        this.usage = "!play <what to play>";
//    }
//
//    @Override
//    public boolean run() {
//        message.delete().queue();
//        if (args.length > 0) {
//            StringBuilder reason = new StringBuilder();
//            for (String arg : args) {
//                reason.append(arg).append(" ");
//            }
//            BotHandler.getBot().getPresence().setActivity(Activity.playing(reason.toString()));
//        } else {
//            channel.sendMessage("**Invalid Arguments:** " + usage).queue();
//        }
//        return true;
//    }
}
