package com.shanebeestudios.bot.listeners;

import com.shanebeestudios.bot.BotHandler;
import com.shanebeestudios.bot.util.Logger;
import net.dv8tion.jda.api.entities.channel.unions.DefaultGuildChannelUnion;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class JoinListener extends ListenerAdapter {

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        String guildID = event.getGuild().getId();

        // Prevent this bot being used on another guild!
        if (!guildID.equalsIgnoreCase(BotHandler.getInstance().getServerID())) {
            Logger.info("Attempting to use bot on guild: " + event.getGuild().getName());
            DefaultGuildChannelUnion channel = event.getGuild().getDefaultChannel();
            if (channel != null) {
                channel.asTextChannel().sendMessage("**You are not authorized to use this bot!!!**").queue();
            }
        }
    }

}
