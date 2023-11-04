package com.shanebeestudios.bot.command;

import com.shanebeestudios.bot.BotHandler;
import com.shanebeestudios.bot.util.Util;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import java.awt.*;
import java.util.Date;
import java.util.EnumSet;
import java.util.Random;

public class CommandRelease extends CommandBase {

    public CommandRelease(BotHandler botHandler, Permission permission) {
        super(botHandler, permission, "Release", "Publish a release");
        botHandler.getGuild().upsertCommand(getCommandName(), getDescription())
                .addOption(OptionType.STRING, "plugin", "Name of plugin", true)
                .addOption(OptionType.STRING, "version", "Version of release", true)
                .addOption(OptionType.STRING, "link", "link for release", true)
                .addOption(OptionType.STRING, "description", "Description of release", true)
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(getPermission()))
                .queue();
    }

    @SuppressWarnings("DataFlowIssue")
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (!event.getName().equalsIgnoreCase("release")) return;
        event.deferReply().queue();

        String plugin = event.getOption("plugin").getAsString();
        String version = event.getOption("version").getAsString();
        String link = event.getOption("link").getAsString();
        String desc = event.getOption("description").getAsString();

        Random random = new Random();
        Color color = new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255));

        MessageEmbed pluginRelease = new EmbedBuilder()
                .setColor(color)
                .setThumbnail(Util.IMAGE_URL)
                .setAuthor("Plugin Release", null, Util.IMAGE_URL)
                .setTitle("**" + plugin + " - " + version + "**")
                .setDescription(desc)
                .addField("**Download:**", link, false)
                .setFooter(this.botHandler.getBotName(), this.botHandler.getBot().getSelfUser().getAvatarUrl())
                .setTimestamp(new Date(System.currentTimeMillis()).toInstant()).build();

        event.getChannel().sendMessageEmbeds(pluginRelease).complete();
        event.getHook().deleteOriginal().queue();
    }

}
