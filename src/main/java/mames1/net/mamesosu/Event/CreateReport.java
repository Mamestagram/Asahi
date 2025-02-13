package mames1.net.mamesosu.Event;

import mames1.net.mamesosu.Utils.Embed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;

public class CreateReport extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent e) {

        if(e.getChannel().getIdLong() != 1336828504989503552L) {
            return;
        }

        if(e.getAuthor().isBot()) {
            return;
        }

        StringSelectMenu.Builder builder = StringSelectMenu.create("menu:report");
        builder.addOption("Cheating", "cheating");
        builder.addOption("Use multiple accounts", "multiaccounting");
        builder.addOption("Inappropriate content", "inappropriate");
        builder.addOption("Other", "other");
        StringSelectMenu menu = builder.build();


        e.getChannel().sendMessageEmbeds(
                Embed.getReportEmbed().build()
        ).setActionRow(
                menu
        ).queue();
    }
}
