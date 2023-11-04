package com.shanebeestudios.bot.command;

import com.shanebeestudios.bot.BotHandler;
import com.shanebeestudios.bot.util.Util;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import java.awt.*;
import java.util.Date;
import java.util.Random;

public class Release extends ListenerAdapter {
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
                .setFooter(BotHandler.getBotName(), BotHandler.getBot().getSelfUser().getAvatarUrl())
                .setTimestamp(new Date(System.currentTimeMillis()).toInstant()).build();

        event.getChannel().sendMessageEmbeds(pluginRelease).complete();
        event.getHook().deleteOriginal().queue();
    }

    public static void registerCommand(Guild guild) {
        guild.upsertCommand("release", "Release a plugin")
                .addOption(OptionType.STRING, "plugin", "Name of plugin", true)
                .addOption(OptionType.STRING, "version", "Version of release", true)
                .addOption(OptionType.STRING, "link", "link for release", true)
                .addOption(OptionType.STRING, "description", "Description of release", true)
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(BotHandler.getInstance().getAdminRole().getPermissions()))
                .queue();
    }

}
