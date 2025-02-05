package mames1.net.mamesosu.Event;

import mames1.net.mamesosu.Utils.Embed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;

public class OpenSupport extends ListenerAdapter {
    // チケットの作成

    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        if (e.getChannel().getName().contains("ticket-")) {
            if(e.getMember().getIdLong() != 557628352828014614L){
                return;
            }

            StringSelectMenu.Builder builder = StringSelectMenu.create("menu:dropdown");
            builder.addOption("Change Email", "change_email");
            builder.addOption("Change Password", "change_password");
            builder.addOption("Change Username", "change_username");
            StringSelectMenu menu = builder.build();

            e.getMessage().replyEmbeds(
                    Embed.getTicketCreatedEmbed(e.getMember()).build()
            ).addComponents(
                    ActionRow.of(menu)
            ).queue();
        }
    }
}
