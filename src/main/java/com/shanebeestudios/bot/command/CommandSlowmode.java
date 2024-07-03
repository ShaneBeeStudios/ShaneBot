package com.shanebeestudios.bot.command;

import com.shanebeestudios.bot.BotHandler;
import com.shanebeestudios.bot.util.Logger;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.channel.attribute.ISlowmodeChannel;
import net.dv8tion.jda.api.entities.channel.unions.GuildChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class CommandSlowmode extends CommandBase {

    public CommandSlowmode(BotHandler botHandler, Permission permission) {
        super(botHandler, permission, "Slowmode", "Set the slowmode of a channel.");
        createCommand(command -> command
            .addOption(OptionType.CHANNEL, "channel", "Channel to add slowmode to", true)
            .addOption(OptionType.INTEGER, "time", "Amount of time", true)
            .addOptions(new OptionData(OptionType.INTEGER, "timeunit", "Unit of time measurement", false)
                .addChoice("seconds", 0)
                .addChoice("minutes", 1)
                .addChoice("hours", 2)));
    }

    @SuppressWarnings("DataFlowIssue")
    @Override
    void onCommand(SlashCommandInteractionEvent event) {
        event.deferReply().setEphemeral(true).queue();

        GuildChannelUnion channel = event.getOption("channel").getAsChannel();
        int time = event.getOption("time").getAsInt();
        OptionMapping timeOption = event.getOption("timeunit");
        int timeU = timeOption != null ? timeOption.getAsInt() : 0;

        TimeUnit timeUnit = switch (timeU) {
            case 1 -> TimeUnit.MINUTES;
            case 2 -> TimeUnit.HOURS;
            default -> TimeUnit.SECONDS;
        };

        if (channel instanceof ISlowmodeChannel textChannel) {
            int seconds = (int) timeUnit.toSeconds(time);
            String message;
            if (seconds <= 0) {
                seconds = 0;
                message = "Slowmode <red>disabled<reset>";
            } else if (seconds > ISlowmodeChannel.MAX_SLOWMODE) {
                seconds = ISlowmodeChannel.MAX_SLOWMODE;
                message = "Slowmode set to <cyan>6 hours<reset>";
            } else {
                String timeSpan = time + " " + timeUnit.toString().toLowerCase(Locale.ROOT);
                message = "Slowmode set to <cyan>" + timeSpan + "<reset>";
            }
            message += " in <purple>" + textChannel.getName() + "<reset>";

            Logger.info(message);

            String finalMessage = message.replaceAll("<\\w+>", "**");
            textChannel.getManager().setSlowmode(seconds).queue(
                success -> event.getHook().editOriginal(finalMessage).queue(),
                fail -> event.getHook().editOriginal("Failed").queue());
        } else {
            event.getHook().editOriginal("Invalid channel for slowmode **" + channel.getName() + "**").queue();
        }
    }

}
