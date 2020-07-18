package com.shanebeestudios.bot.task;

import com.shanebeestudios.bot.util.Logger;

import java.util.Scanner;

public class ConsoleThread extends Thread {

    public ConsoleThread(String name) {
        super(name);
    }

    @Override
    public void run() {
        Scanner sc = new Scanner(System.in);

        while(sc.hasNextLine()) {
            if (sc.next().equalsIgnoreCase("stop")) {
                Logger.info("Stopping server!");
                System.exit(0);
            }
        }
    }

}
