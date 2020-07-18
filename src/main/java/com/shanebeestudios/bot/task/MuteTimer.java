package com.shanebeestudios.bot.task;

import com.shanebeestudios.bot.BotHandler;
import com.shanebeestudios.bot.util.Logger;

import java.util.Timer;
import java.util.TimerTask;

public class MuteTimer extends TimerTask {

    private BotHandler botHandler;
    private long t;

    public MuteTimer(BotHandler botHandler, int seconds) {
        this.botHandler = botHandler;
        Logger.info("Starting timer...");
        Timer timer = new Timer("MuteTimer");
        timer.schedule(this, 0, seconds * 1000);
    }

    @Override
    public void run() {
        tick();
    }

    private void tick() {
        botHandler.getMuteData().checkMutes();
    }

}
