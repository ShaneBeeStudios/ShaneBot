package com.shanebeestudios.bot.listeners;

import com.shanebeestudios.bot.BotHandler;
import com.shanebeestudios.bot.util.Logger;
import com.shanebeestudios.bot.util.MemberUtil;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class JoinListener extends ListenerAdapter {

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        String ID = event.getGuild().getId();

        // Prevent this bot being used on another guild!
        if (!ID.equalsIgnoreCase(BotHandler.getINSTANCE().getServerID())) {
            Logger.info("Attempting to use bot on guild: " + event.getGuild().getName());
            TextChannel channel = event.getGuild().getDefaultChannel();
            if (channel != null) {
                channel.sendMessage("**You are not authorized to use this bot!!!**").queue();
            }
            return;
        }

        Member member = event.getMember();
        // Discord now handles this, YAY!
        // But let's keep it here for safe keeping
//        TextChannel welcomeChannel = BotHandler.getINSTANCE().getWelcomeChannel(); // TEMP for testing
//        TextChannel rulesChannel = BotHandler.getINSTANCE().getRulesChannel(); // TEMP for testing
//
//
//        welcomeChannel.sendMessage("Welcome " + member.getAsMention() + " to **" + event.getGuild().getName() + "**, Please " +
//                "make sure to read over " + rulesChannel.getAsMention() +
//                " ... not knowing the rules for this Discord guild will not be an excuse when a rule is broken!").queue();

        // If a user joins that was previously muted, re-add the muted role
        if (BotHandler.getINSTANCE().getMuteData().isMuted(member)) {
            MemberUtil.addMuteRole(member);
        }
    }

}
