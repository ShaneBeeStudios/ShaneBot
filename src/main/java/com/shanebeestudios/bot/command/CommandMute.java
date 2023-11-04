package com.shanebeestudios.bot.command;

import com.shanebeestudios.bot.BotHandler;
import com.shanebeestudios.bot.util.MemberUtil;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.concurrent.TimeUnit;

public class CommandMute extends CommandBase {
    public CommandMute(BotHandler botHandler, Permission permission) {
        super(botHandler, permission, "Mute", "Mute a user with a message sent to them and the bot channel.");
        botHandler.getGuild().upsertCommand(getCommandName(), getDescription())
                .addOption(OptionType.USER, "member", "Member to mute", true)
                .addOption(OptionType.INTEGER, "time", "Time something something", true)
                .addOptions(new OptionData(OptionType.INTEGER, "timeunit", "Unit of time measurement", true)
                        .addChoice("minutes", 0)
                        .addChoice("hours", 1)
                        .addChoice("days", 2))
                .addOption(OptionType.STRING, "reason", "Reason for mute", true)
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(getPermission()))
                .queue();
    }

    @SuppressWarnings("DataFlowIssue")
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (!event.getName().equalsIgnoreCase(getCommandName())) return;
        event.deferReply().queue();

        Member member = event.getOption("member").getAsMember();
        int time = event.getOption("time").getAsInt();
        int timeU = event.getOption("timeunit").getAsInt();
        String reason = event.getOption("reason").getAsString();

        TimeUnit timeUnit = switch (timeU) {
            case 1 -> TimeUnit.HOURS;
            case 2 -> TimeUnit.DAYS;
            default -> TimeUnit.MINUTES;
        };

        MemberUtil.timeoutMember(member, time, timeUnit, reason, event.getMember());
        event.getHook().deleteOriginal().queue();
    }

}
