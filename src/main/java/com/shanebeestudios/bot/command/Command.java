package com.shanebeestudios.bot.command;

import com.shanebeestudios.bot.BotHandler;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

/**
 * Base command class for all commands
 */
public abstract class Command {

    protected Message message;
    protected TextChannel channel;
    protected Member member;
    protected String[] args;
    private final boolean requiresAdmin;

    public Command(boolean requiresAdmin) {
        this.requiresAdmin = requiresAdmin;
    }

    public boolean run(MessageReceivedEvent event, String[] args) {
        this.channel = event.getTextChannel();
        this.member = event.getMember();
        if (member == null) return true;

        if (requiresAdmin && !member.getRoles().contains(BotHandler.getINSTANCE().getAdminRole())) {
            return true;
        }
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
