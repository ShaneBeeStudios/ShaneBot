package com.shanebeestudios.bot.command;

import com.shanebeestudios.bot.BotHandler;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class Playing extends ListenerAdapter {
    @SuppressWarnings("DataFlowIssue")
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (!event.getName().equalsIgnoreCase("activity")) return;
        event.deferReply().queue();

        int ac = event.getOption("activity").getAsInt();
        String what = event.getOption("what").getAsString();
        String stream = "";
        OptionMapping stream1 = event.getOption("stream");
        if (stream1 != null) stream = stream1.getAsString();

        Activity activity = switch (ac) {
            case 1 -> Activity.streaming(what, stream);
            case 2 -> Activity.listening(what);
            case 3 -> Activity.watching(what);
            case 4 -> Activity.customStatus(what);
            case 5 -> Activity.competing(what);
            default -> Activity.playing(what);
        };
        BotHandler.getBot().getPresence().setActivity(activity);

        event.getHook().deleteOriginal().queue();
    }

    public static void registerCommand(Guild guild) {
        guild.upsertCommand("activity", "Set the activity of the bot")
                .addOptions(new OptionData(OptionType.INTEGER, "activity", "Which activity to use", true, true)
                        .addChoice("playing", 0)
                        .addChoice("streaming", 1)
                        .addChoice("listening", 2)
                        .addChoice("watching", 3)
                        .addChoice("custom", 4)
                        .addChoice("competing", 5))
                .addOption(OptionType.STRING, "what", "What to play", true)
                .addOption(OptionType.STRING, "stream", "Link if streaming")
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(BotHandler.getInstance().getAdminRole().getPermissions()))
                .queue();
    }

}
