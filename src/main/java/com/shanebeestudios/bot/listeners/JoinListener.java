package com.shanebeestudios.bot.listeners;

import com.shanebeestudios.bot.BotHandler;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class JoinListener extends ListenerAdapter {

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        Member member = event.getMember();
        TextChannel welcomeChannel = BotHandler.getINSTANCE().getWelcomeChannel(); // TEMP for testing
        TextChannel rulesChannel = BotHandler.getINSTANCE().getRulesChannel(); // TEMP for testing

        welcomeChannel.sendMessage("Welcome " + member.getAsMention() + " to **" + event.getGuild().getName() + "**, Please " +
                "make sure to read over " + rulesChannel.getAsMention() +
                " ... not knowing the rules for this Discord guild will not be an excuse when a rule is broken!");
    }

}
