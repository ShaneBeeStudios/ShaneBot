package com.shanebeestudios.bot.command;

import com.shanebeestudios.bot.BotHandler;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.function.Consumer;

/**
 * Base command class for all commands
 */
public abstract class Command {

    Message message;
    TextChannel channel;
    Member member;
    String[] args;
    final Permission permission;
    String description = "";
    String usage = "";

    public Command(Permission permission) {
        this.permission = permission;
    }

    public boolean run(MessageReceivedEvent event, String[] args) {
        this.channel = event.getTextChannel();
        this.member = event.getMember();
        this.message = event.getMessage();
        if (member == null) return true;

        if (!hasPermission(member)) {
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
     * @param position Position in command args
     * @param member Consumer to do stuff with the retrieved member
     */
    @SuppressWarnings("SameParameterValue")
    void parseMember(int position, Consumer<Member> member) {
        if (args.length > position) {
            String memberString = args[position].replace("<@!", "").replace(">", "");
            channel.getGuild().retrieveMemberById(memberString).queue(member, fail ->
                    channel.sendMessage("Invalid Member: " + args[0]).queue());
        }
    }

    /**
     * Get the description of this command
     *
     * @return Description of this command
     */
    public String getDescription() {
        return description;
    }

    /**
     * Get the usage of this command
     *
     * @return Usage of this command
     */
    public String getUsage() {
        return usage;
    }

    /**
     * Check if this member has permission to use this comamnd
     *
     * @param member Member to check permission for
     * @return True if the member has permission to use this command
     */
    public boolean hasPermission(Member member) {
        if (permission == Permission.OWNER && !member.isOwner()) {
            return false;
        }
        return permission != Permission.ADMIN || member.getRoles().contains(BotHandler.getINSTANCE().getAdminRole()) || member.isOwner();
    }

    public enum Permission {
        OWNER(10),
        ADMIN(9),
        NONE(0);

        private final int level;

        Permission(int level) {
            this.level = level;
        }

        public int getLevel() {
            return level;
        }
    }

}
