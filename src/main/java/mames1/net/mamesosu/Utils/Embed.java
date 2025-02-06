package mames1.net.mamesosu.Utils;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;

import java.awt.*;

public abstract class Embed {

    public static EmbedBuilder getTicketCreatedEmbed() {

        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("**Hello, Mamestagram player!**");
        eb.addField(
                "**<:heart:1286012142683820126> Welcome to Mamestagram Support!**",
                "* You can easily change your password and update your registered information, such as your email, using this bot. \n" +
                        "* For any other inquiries, please send a message with your request and wait for our staff to respond. \n" +
                        "* We will get back to you within 24 hours.",
                false
        );
        eb.setColor(Color.BLACK);

        return eb;

    }

    public static EmbedBuilder getApprovedEmailChangeRequest() {
        EmbedBuilder eb = new EmbedBuilder();
        eb.addField(
                "**<:check:1285853854667112480> The email change request has been approved!**",
                "```Please enter your new email!```",
                false
        );
        eb.setColor(Color.BLACK);

        return eb;
    }

    public static EmbedBuilder getVerificationEmailEmbed(String email) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.addField(
                "**<:check:1285853854667112480> Verification email has been sent!**",
                "* To: ``" + email + "``",
                false
        );
        eb.setColor(Color.BLACK);

        return eb;
    }

    public static EmbedBuilder getApprovedPasswordChangeRequest() {
        EmbedBuilder eb = new EmbedBuilder();
        eb.addField(
                "**<:check:1285853854667112480> The password change request has been approved!**",
                "```Please enter your new password!```",
                false
        );
        eb.setColor(Color.BLACK);

        return eb;
    }

    public static EmbedBuilder getChangedPasswordEmbed() {
        EmbedBuilder eb = new EmbedBuilder();
        eb.addField(
                "**<:check:1285853854667112480> Password has been changed!**",
                "```Your password has been successfully changed.```",
                false
        );
        eb.setColor(Color.BLACK);

        return eb;
    }

    public static EmbedBuilder getChangedEmailEmbed(String email) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.addField(
                "**<:check:1285853854667112480> Email has been changed!**",
                "```" + email + "```",
                false
        );
        eb.setColor(Color.BLACK);

        return eb;
    }

    public static EmbedBuilder getErrorEmbed(String message) {

        EmbedBuilder eb = new EmbedBuilder();
        eb.addField("**<:warning:1285853296833335366> Error**", "```" + message + "```", false);
        eb.setColor(Color.BLACK);

        return eb;
    }
}
