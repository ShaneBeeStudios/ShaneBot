package com.shanebeestudios.bot.command;

public class Say extends Command {

    public Say(boolean requiresAdmin) {
        super(requiresAdmin);
        this.description = "Make the bot say a message";
        this.usage = "!say <message>";
    }

    @Override
    public boolean run() {
        message.delete().queue();
        if (args.length > 0) {
            StringBuilder toSay = new StringBuilder();
            for (int i = 0; i < args.length; i++) {
                toSay.append(args[i]).append(" ");
            }

            channel.sendMessage(toSay.toString()).queue();
        } else {
            channel.sendMessage("**Incorrect Arguments:** " + getUsage()).queue();
        }
        return true;
    }

}
