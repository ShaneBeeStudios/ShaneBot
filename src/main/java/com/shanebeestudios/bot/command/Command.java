package com.shanebeestudios.bot.command;

import com.shanebeestudios.bot.BotHandler;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

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
        this.message = event.getMessage();
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
     */
    @SuppressWarnings("SameParameterValue")
    protected void parseMember(int position, Consumer<Member> member) {
        if (args.length > position) {
            String memberString = args[position].replace("<@!", "").replace(">", "");
            channel.getGuild().retrieveMemberById(memberString).queue(member, fail ->
                    channel.sendMessage("Invalid Member: " + args[0]).queue());
        }
    }

}
