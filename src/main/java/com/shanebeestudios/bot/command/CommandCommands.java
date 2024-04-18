package com.shanebeestudios.bot.command;

import com.shanebeestudios.bot.BotHandler;
import com.shanebeestudios.bot.util.Util;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;

import java.awt.*;
import java.util.Date;

public class CommandCommands extends CommandBase {

    public CommandCommands(BotHandler botHandler, Permission permission) {
        super(botHandler, permission, "Commands", "See a list of all commands");
        createCommand();
    }

    @Override
    public void onCommand(SlashCommandInteractionEvent event) {
        Member member = event.getMember();
        if (member == null) return;

        String botAvatar = botHandler.getBot().getSelfUser().getAvatarUrl();
        String botName = botHandler.getBotName();
        EmbedBuilder embedBuilder = new EmbedBuilder()
            .setColor(new Color(0, 255, 197))
            .setThumbnail(botAvatar)
            .setAuthor("Commands", null, Util.IMAGE_URL)
            .setTitle("**" + botName + "'s Commands**")
            .setDescription("All available commands from " + botName + " that you have permission to use.");

        botHandler.getCommands().forEach(command -> {
            if (!(command instanceof CommandCommands) && member.hasPermission(command.getPermission())) {
                embedBuilder.addField(command.getName(), command.getDescription(), false);
            }
        });

        embedBuilder
            .setFooter(botName, botAvatar)
            .setTimestamp(new Date(System.currentTimeMillis()).toInstant());

        event.replyEmbeds(embedBuilder.build()).setEphemeral(true).queue();
    }
}
