package mames1.net.mamesosu;

import mames1.net.mamesosu.Object.Bot;
import mames1.net.mamesosu.Object.Ticket;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static Bot bot;
    public static List<Ticket> tickets;

    public static void main(String[] args) {
        bot = new Bot();
        tickets = new ArrayList<>();

        bot.start();
    }
}