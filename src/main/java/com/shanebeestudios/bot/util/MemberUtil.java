package com.shanebeestudios.bot.util;

import com.shanebeestudios.bot.BotHandler;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;

public class MemberUtil {

    public static void addMutedRole(Member member) {
        Role role = BotHandler.getINSTANCE().getMutedRole();
        if (role != null) {
            member.getGuild().addRoleToMember(member, role).queue();
        }
    }

    public static void removeMutedRole(Member member) {
        Role role = BotHandler.getINSTANCE().getMutedRole();
        if (role != null) {
            member.getGuild().removeRoleFromMember(member, role).queue();
        }
    }

    public static void directMessage(Member member, MessageEmbed message) {
        member.getUser().openPrivateChannel().queue(s -> s.sendMessage(message));
    }

}
