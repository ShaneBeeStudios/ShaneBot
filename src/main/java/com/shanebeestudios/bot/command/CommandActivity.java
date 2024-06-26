package com.shanebeestudios.bot.command;

import com.shanebeestudios.bot.BotHandler;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.Locale;

public class CommandActivity extends CommandBase {

    public CommandActivity(BotHandler botHandler, Permission permission) {
        super(botHandler, permission, "Activity", "Set the activity of the bot");
        createCommand(command -> command
            .addOptions(new OptionData(OptionType.INTEGER, "activity", "Which activity to use", true)
                .addChoice("playing", 0)
                .addChoice("streaming", 1)
                .addChoice("listening", 2)
                .addChoice("watching", 3)
                .addChoice("custom", 4)
                .addChoice("competing", 5))
            .addOption(OptionType.STRING, "what", "What to play", true)
            .addOption(OptionType.STRING, "stream", "Link if streaming"));
    }

    @SuppressWarnings("DataFlowIssue")
    @Override
    public void onCommand(SlashCommandInteractionEvent event) {
        event.deferReply().setEphemeral(true).queue();

        int ac = event.getOption("activity").getAsInt();
        String what = event.getOption("what").getAsString();
        String stream = "";
        OptionMapping streamOption = event.getOption("stream");
        if (streamOption != null) stream = streamOption.getAsString();

        Activity activity = switch (ac) {
            case 1 -> Activity.streaming(what, stream);
            case 2 -> Activity.listening(what);
            case 3 -> Activity.watching(what);
            case 4 -> Activity.customStatus(what);
            case 5 -> Activity.competing(what);
            default -> Activity.playing(what);
        };
        this.botHandler.getBot().getPresence().setActivity(activity);

        String type = "";
        if (activity.getType() != Activity.ActivityType.CUSTOM_STATUS) {
            String suffix = switch (activity.getType()) {
                default -> " ";
                case COMPETING -> " in ";
                case LISTENING -> " to ";
            };
            type = activity.getType().toString().toLowerCase(Locale.ROOT) + suffix;
        }
        event.getHook().editOriginal("I am now **" + type + what + "**").queue();
    }

}
