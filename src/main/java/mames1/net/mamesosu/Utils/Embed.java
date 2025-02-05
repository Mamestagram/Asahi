package mames1.net.mamesosu.Utils;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;

import java.awt.*;

public abstract class Embed {

    public static EmbedBuilder getTicketCreatedEmbed(Member m) {

        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Hello, " + m.getNickname() + "!");
        eb.setDescription("""
                <:heart:1286012142683820126> Welcome to Mamestagram Support!\s
                You can easily change your password and update your registered information, such as your email, using this bot. \s
                For any other inquiries, please send a message with your request and wait for our staff to respond. \s
                We will get back to you within 24 hours.
               \s""");
        eb.setColor(Color.BLACK);

        return eb;

    }
}
