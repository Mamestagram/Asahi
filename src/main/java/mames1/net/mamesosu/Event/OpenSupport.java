package mames1.net.mamesosu.Event;

import mames1.net.mamesosu.Utils.Embed;
import net.dv8tion.jda.api.events.channel.ChannelCreateEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;

public class OpenSupport extends ListenerAdapter {

    @Override
    public void onChannelCreate(ChannelCreateEvent e) {
        if (e.getChannel().getName().contains("ticket-")) {

            StringSelectMenu.Builder builder = StringSelectMenu.create("menu:dropdown");
            builder.addOption("Change Email", "change_email");
            builder.addOption("Change Password", "change_password");
            StringSelectMenu menu = builder.build();

            e.getChannel().asTextChannel().sendMessageEmbeds(
                    Embed.getTicketCreatedEmbed().build()
            ).setActionRow(
                    menu
            ).queue();
        }
    }
}
