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
    private final List<Activity> springActivities = new ArrayList<>();
    private final List<Activity> summerActivities = new ArrayList<>();
    private final List<Activity> fallActivities = new ArrayList<>();
    private final List<Activity> octoberActivities = new ArrayList<>();
    private final List<Activity> decemberActivities = new ArrayList<>();
    private final List<Activity> currentActivities = new ArrayList<>();
    private final Random random;
    private final Timer timer;
    private Month currentMonth;

    public ActivityTask(BotHandler botHandler, long minutes) {
        this.bot = botHandler.getBot();
        this.random = new Random();

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
        this.generalActivities.add(Activity.watching("reruns of Friends"));
        this.generalActivities.add(Activity.watching("that new show everyone is talkin about"));

        // Special Monthly Activities
        // Spring Activities
        this.springActivities.add(Activity.customStatus("Planting a garden"));
        this.springActivities.add(Activity.customStatus("Smelling the flowers"));
        this.springActivities.add(Activity.customStatus("Having a picnic"));
        this.springActivities.add(Activity.customStatus("Having a BBQ"));
        this.springActivities.add(Activity.customStatus("Feeding the ducks"));
        this.springActivities.add(Activity.customStatus("Riding my bike"));
        this.springActivities.add(Activity.playing("catch"));

        // Summer Activities
        this.summerActivities.add(Activity.competing("the Summer Olympics"));
        this.summerActivities.add(Activity.customStatus("Getting a tan"));
        this.summerActivities.add(Activity.customStatus("Laying by the pool"));
        this.summerActivities.add(Activity.customStatus("Building a sandcastle at the beach"));
        this.summerActivities.add(Activity.playing("at the beach"));

        // Fall Activities
        this.fallActivities.add(Activity.customStatus("Apple picking"));
        this.fallActivities.add(Activity.customStatus("Attending a fall festival"));
        this.fallActivities.add(Activity.customStatus("Going camping."));
        this.fallActivities.add(Activity.customStatus("Having a bonfire"));
        this.fallActivities.add(Activity.customStatus("Laying in a meadow"));
        this.fallActivities.add(Activity.customStatus("Raking up the leaves"));
        this.fallActivities.add(Activity.customStatus("Visiting a farmers market"));
        this.fallActivities.add(Activity.playing("outside in the leaves"));

        // October Activities
        this.octoberActivities.add(Activity.competing("a costume contest"));
        this.octoberActivities.add(Activity.competing("a costume party"));
        this.octoberActivities.add(Activity.competing("a pumpkin carving contest"));
        this.octoberActivities.add(Activity.customStatus("Carving a pumpkin"));
        this.octoberActivities.add(Activity.customStatus("Throwing pumpkins"));
        this.octoberActivities.add(Activity.customStatus("Trick or Treating"));
        this.octoberActivities.add(Activity.customStatus("Trying on costumes"));
        this.octoberActivities.add(Activity.watching("a scary movie"));

        // December Activities
        this.decemberActivities.add(Activity.competing("the Winter Olympics"));
        this.decemberActivities.add(Activity.customStatus("Building a snowman"));
        this.decemberActivities.add(Activity.customStatus("Decorating the Christmas tree"));
        this.decemberActivities.add(Activity.customStatus("Plowing the driveway"));
        this.decemberActivities.add(Activity.customStatus("Wrapping presents"));
        this.decemberActivities.add(Activity.listening("Christmas music"));
        this.decemberActivities.add(Activity.playing("outside in the snow"));
        this.decemberActivities.add(Activity.watching("a Hallmark Christmas movie"));

        this.updateMonthlyActivities(LocalDate.now(Clock.systemDefaultZone()).getMonth());

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
        if (month == Month.APRIL || month == Month.MAY) {
            this.currentActivities.addAll(this.springActivities);
        } else if (month == Month.JULY || month == Month.AUGUST) {
            this.currentActivities.addAll(this.summerActivities);
        } else if (month == Month.SEPTEMBER || month == Month.OCTOBER) {
            this.currentActivities.addAll(this.fallActivities);
            if (month == Month.OCTOBER) {
                this.currentActivities.addAll(this.octoberActivities);
            }
        } else if (month == Month.DECEMBER) {
            this.currentActivities.addAll(this.decemberActivities);
        }
    }

    public void cancelTimer() {
        this.timer.cancel();
    }

}
