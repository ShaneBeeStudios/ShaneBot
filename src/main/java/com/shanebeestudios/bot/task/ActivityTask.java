package com.shanebeestudios.bot.task;

import com.shanebeestudios.bot.BotHandler;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;

import java.time.Clock;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class ActivityTask extends TimerTask {

    private final JDA bot;
    private final List<Activity> generalActivities = new ArrayList<>();
    private final List<Activity> summerActivities = new ArrayList<>();
    private final List<Activity> octoberActivities = new ArrayList<>();
    private final List<Activity> decemberActivities = new ArrayList<>();
    private final List<Activity> currentActivities = new ArrayList<>();
    private final Random random;
    private final Timer timer;
    private Month currentMonth;

    public ActivityTask(BotHandler botHandler, long minutes) {
        this.bot = botHandler.getBot();
        this.random = new Random();
        LocalDate date = LocalDate.now(Clock.systemDefaultZone());
        this.currentMonth = date.getMonth();

        // General Activities
        this.generalActivities.add(Activity.competing("a poker tournament"));
        this.generalActivities.add(Activity.listening("soft jazz"));
        this.generalActivities.add(Activity.listening("the radio"));
        this.generalActivities.add(Activity.listening("top 40"));
        this.generalActivities.add(Activity.playing("Fortnite"));
        this.generalActivities.add(Activity.playing("Minecraft"));
        this.generalActivities.add(Activity.playing("Monopoly"));
        this.generalActivities.add(Activity.playing("the drums"));
        this.generalActivities.add(Activity.playing("Uncharted 4"));
        this.generalActivities.add(Activity.playing("with fire"));
        this.generalActivities.add(Activity.watching("reruns of Freinds"));
        this.generalActivities.add(Activity.watching("that new show everyone is talkin about"));
        this.currentActivities.addAll(this.generalActivities);

        // Special Monthly Activities
        // Summer Activities
        this.summerActivities.add(Activity.competing("the Summer Olympics"));
        this.summerActivities.add(Activity.customStatus("Getting a tan"));
        this.summerActivities.add(Activity.customStatus("Laying by the pool"));
        this.summerActivities.add(Activity.customStatus("Building a sandcastle at the beach"));
        this.summerActivities.add(Activity.playing("at the beach"));
        if (this.currentMonth == Month.JULY || this.currentMonth == Month.AUGUST) this.currentActivities.addAll(this.summerActivities);

        // October Activities
        this.octoberActivities.add(Activity.competing("a costume contest"));
        this.octoberActivities.add(Activity.competing("a pumpkin carving contest"));
        this.octoberActivities.add(Activity.customStatus("Carving a pumpkin"));
        this.octoberActivities.add(Activity.customStatus("Throwing pumpkins"));
        this.octoberActivities.add(Activity.customStatus("Trick or Treating"));
        this.octoberActivities.add(Activity.customStatus("Trying on costumes"));
        this.octoberActivities.add(Activity.playing("outside in the leaves"));
        this.octoberActivities.add(Activity.watching("a scary movie"));
        if (this.currentMonth == Month.OCTOBER) this.currentActivities.addAll(this.octoberActivities);

        // December Activities
        this.decemberActivities.add(Activity.playing("outside in the snow"));
        this.decemberActivities.add(Activity.competing("the Winter Olympics"));
        this.decemberActivities.add(Activity.customStatus("Building a snowman"));
        this.decemberActivities.add(Activity.customStatus("Decorating the Christmas tree"));
        this.decemberActivities.add(Activity.customStatus("Wrapping presents"));
        this.decemberActivities.add(Activity.listening("Christmas music"));
        this.decemberActivities.add(Activity.watching("a Hallmark Christmas movie"));
        if (this.currentMonth == Month.DECEMBER) this.currentActivities.addAll(this.decemberActivities);

        this.timer = new Timer("activity timer");
        this.timer.schedule(this, 0, minutes * 60 * 1000); // Every 5 minutes
    }

    @Override
    public void run() {
        LocalDate date = LocalDate.now(Clock.systemDefaultZone());
        Month month = date.getMonth();
        int dayOfMonth = date.getDayOfMonth();
        Activity activity;

        // Update currentActivities list if month changed
        if (this.currentMonth != month) {
            updateMonthlyActivities(month);
        }

        // Special Day Activity - All other activities will be ignored
        if (month == Month.JANUARY && dayOfMonth == 1) {
            activity = Activity.customStatus("Wishing you a Happy New Year");
        } else if (month == Month.FEBRUARY && dayOfMonth == 14) {
            activity = Activity.customStatus("Wishing you a Lovely Valentines Day");
        } else if (month == Month.MARCH && dayOfMonth == 17) {
            activity = Activity.customStatus("Wishing you a Lucky St.Paddy's Day");
        } else if (month == Month.OCTOBER && dayOfMonth == 31) {
            activity = Activity.customStatus("Wishing you a Spooky Halloween");
        } else if (month == Month.DECEMBER && dayOfMonth == 25) {
            activity = Activity.customStatus("Wishing you a Merry Christmas");
        } else {
            int i = this.random.nextInt(this.currentActivities.size());
            activity = this.currentActivities.get(i);
        }
        this.bot.getPresence().setActivity(activity);
    }

    private void updateMonthlyActivities(Month month) {
        this.currentMonth = month;
        this.currentActivities.clear();
        this.currentActivities.addAll(this.generalActivities);
        if (month == Month.JULY || month == Month.AUGUST) {
           this.currentActivities.addAll(this.summerActivities);
        } else if (month == Month.OCTOBER) {
            this.currentActivities.addAll(this.octoberActivities);
        } else if (month == Month.DECEMBER) {
            this.currentActivities.addAll(this.decemberActivities);
        }
    }

    public void cancelTimer() {
        this.timer.cancel();
    }

}
