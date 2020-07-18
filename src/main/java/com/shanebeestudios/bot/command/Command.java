package com.shanebeestudios.bot.command;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

/**
 * Base command class for all commands
 */
public abstract class Command {

    protected Message message;
    protected TextChannel channel;
    protected Member member;
    protected String[] args;

    public Command() {
    }

    public boolean run(MessageReceivedEvent event, String[] args) {
        this.message = event.getMessage();
        this.channel = event.getTextChannel();
        this.member = event.getMember();
        this.args = args;
        return run();
    }

    public boolean run() {
        return true;
    }

    /**
     * Parse a Member at a specific position in the command args
     *
     * @param position Potion in command args
     * @return Member if found, null otherwise
     */
    protected Member parseMember(int position) {
        if (args.length > position) {
            String memberString = args[position].replace("<@!", "").replace(">", "");
            return channel.getGuild().getMemberById(memberString);
        }
        return null;
    }

}
