package mames1.net.mamesosu.Utils;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;

import java.awt.*;
import java.util.Date;

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
        eb.setTimestamp(new Date().toInstant());

        return eb;

    }

    public static EmbedBuilder getReportEmbed() {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("**Report a Player!**");
        eb.addField("**<:warning:1285853296833335366> Have you found a player violating the rules?**",
            "* Select a reason from the dropdown, fill in the form with details, and report anonymously.", false);
        eb.setColor(Color.BLACK);

        return eb;
    }

    public static EmbedBuilder getRestrictEmbed(int id, String player, String reason, Member admin) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setAuthor(player, "https://a.mamesosu.net/" + id, "https://mamesosu.net/u/" + id);
        eb.setTitle("**<:lockkeyhole:1286162425338527795> <" + player + " (" + id + ")> has been restricted!**");
        eb.setThumbnail("https://a.mamesosu.net/" + id);
        eb.addField("> <:question:1285854271857889291> **Reason**", "```" + reason + "```", true);
        eb.addField("> <:peoplegroup:1285955898124140575> **Admin**", admin.getAsMention(), true);
        eb.setFooter("Restricted at " + new Date().toInstant());

        return eb;
    }

    public static EmbedBuilder getReportSubmittedEmbed() {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("**Report Submitted!**");
        eb.setDescription("```Your report has been submitted.```");
        eb.setColor(Color.BLACK);
        eb.setTimestamp(new Date().toInstant());

        return eb;
    }

    public static EmbedBuilder getReportReceivedEmbed(Member member, String player, String reason, String comment) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("**Report Received!**");
        eb.setDescription("A report has been submitted by " + member.getAsMention() + "!");
        eb.addField("Player", "```" + player + "```" , true);
        eb.addField("Reason", "```" + reason + "```" , true);
        eb.addField("Comments", "```"  + comment + "```" , true);
        eb.setColor(Color.BLACK);
        eb.setTimestamp(new Date().toInstant());

        return eb;
    }

    public static EmbedBuilder getAcceptedReportEmbed(String comment, String player) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("**<:check:1285853854667112480> Your report is accepted!**");
        eb.addField(
                "Comment (" +  player + ")", "```" + comment + "```", false
        );
        eb.setColor(Color.BLACK);
        eb.setTimestamp(new Date().toInstant());

        return eb;
    }

    public static EmbedBuilder getDeniedReportEmbed(String comment, String player) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("**<:X_:1286157493378224191> Your report is denied!**");
        eb.addField(
                "Comment (" + player + ")", "```" + comment+ "```", false
        );
        eb.setColor(Color.BLACK);
        eb.setTimestamp(new Date().toInstant());

        return eb;
    }

    public static EmbedBuilder getApprovedEmailChangeRequest() {
        EmbedBuilder eb = new EmbedBuilder();

        eb.setTitle("**The email change request has been approved!**");
        eb.setDescription("```Please enter your new email!```");
        eb.setColor(Color.BLACK);
        eb.setTimestamp(new Date().toInstant());

        return eb;
    }

    public static EmbedBuilder getVerificationEmailEmbed(String email) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("**Verification email has been sent!**");
        eb.setDescription("```" + email + "```");
        eb.setColor(Color.BLACK);
        eb.setTimestamp(new Date().toInstant());

        return eb;
    }

    public static EmbedBuilder getApprovedPasswordChangeRequest() {
        EmbedBuilder eb = new EmbedBuilder();

        eb.setTitle("**The password change request has been approved!**");
        eb.setDescription("```Please enter your new password!```");
        eb.setColor(Color.BLACK);
        eb.setTimestamp(new Date().toInstant());

        return eb;
    }

    public static EmbedBuilder getChangedPasswordEmbed() {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("**Password has been changed!**");
        eb.setDescription("```Your password has been successfully changed.```");
        eb.setColor(Color.BLACK);
        eb.setTimestamp(new Date().toInstant());

        return eb;
    }

    public static EmbedBuilder getChangedEmailEmbed(String email) {
        EmbedBuilder eb = new EmbedBuilder();

        eb.setTitle("**Email has been changed!**");
        eb.setDescription("```" + email + "```");
        eb.setColor(Color.BLACK);
        eb.setTimestamp(new Date().toInstant());

        return eb;
    }

    public static EmbedBuilder getErrorEmbed(String message) {

        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("**Error**");
        eb.setDescription("```" + message + "```");
        eb.setColor(Color.BLACK);
        eb.setTimestamp(new Date().toInstant());

        return eb;
    }
}
