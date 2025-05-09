package mames1.net.mamesosu.Event;

import mames1.net.mamesosu.Main;
import mames1.net.mamesosu.Object.MySQL;
import mames1.net.mamesosu.Object.Report;
import mames1.net.mamesosu.Object.Ticket;
import mames1.net.mamesosu.Utils.Embed;
import mames1.net.mamesosu.Utils.Modal;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReportPlayer extends ListenerAdapter {


    // 制限のリクエストについて許可したかの確認
    @Override
    public void onMessageReceived(MessageReceivedEvent e) {

        if (e.getAuthor().isBot()) {
            return;
        }

        if (!e.getChannelType().isGuild()) {
            return;
        }

        if(e.getChannel().getIdLong() != 1336992233378943050L) {
            return;
        }

        e.getMessage().addReaction(
                Emoji.fromUnicode("U+2753")
        ).queue();
    }

    @Override
    public void onStringSelectInteraction(StringSelectInteractionEvent e) {

        if(e.getComponentId().equals("menu:report")) {

            Main.reports.add((new Report(e.getMember(), e.getSelectedOptions().get(0).getValue(), null)));

            TextInput player = Modal.createTextInput("player", "Player", "Please provide the player's username.", true, TextInputStyle.SHORT);

            TextInput reason = Modal.createTextInput("comments", "Comments", "Please provide any information you believe could be useful.", true, TextInputStyle.PARAGRAPH);
            e.replyModal(net.dv8tion.jda.api.interactions.modals.Modal.create(
                    "report_player", "Report a player"
                ).addActionRows(
                    ActionRow.of(player),
                    ActionRow.of(reason)
                ).build()
            ).queue();
        }
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent e) {

        if(e.getComponentId().equals("report:accept")) {
            TextInput comment = Modal.createTextInput("comment", "Comment", "Please provide a comment.", true, TextInputStyle.PARAGRAPH);

            e.replyModal(net.dv8tion.jda.api.interactions.modals.Modal.create(
                    "report_accept", "Accept report"
                ).addActionRows(
                    ActionRow.of(comment)
                ).build()
            ).queue();
        } else if(e.getComponentId().equals("report:deny")) {
            TextInput comment = Modal.createTextInput("comment", "Comment", "Please provide a comment.", true, TextInputStyle.PARAGRAPH);

            e.replyModal(net.dv8tion.jda.api.interactions.modals.Modal.create(
                    "report_deny", "Deny report"
                ).addActionRows(
                    ActionRow.of(comment)
                ).build()
            ).queue();
        }
    }

    @Override
    public void onModalInteraction(ModalInteractionEvent e) {

        if (e.getModalId().equals("report_player")) {
            MySQL mySQL = new MySQL();
            Report report = Main.reports.stream().filter(r -> r.getReporter() == e.getMember()).findFirst().orElse(null);

            if (report == null) {
                e.replyEmbeds(
                        Embed.getErrorEmbed("Unexpected error occurred. Please try again.").build()
                ).setEphemeral(true).queue();
                return;
            }

            try {
                Connection connection = mySQL.getConnection();
                PreparedStatement ps;
                ResultSet result;
                ps = connection.prepareStatement(
                        "select * from users where name = ?"
                );
                ps.setString(1, e.getValue("player").getAsString());
                result = ps.executeQuery();

                if(!result.next()) {
                        e.replyEmbeds(
                            Embed.getErrorEmbed("The entered player could not be found. Please enter a valid username.").build()
                    ).setEphemeral(true).queue();
                    return;
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            e.replyEmbeds(Embed.getReportSubmittedEmbed().build()).setEphemeral(true).queue();
            e.getJDA().getTextChannelById(1336992233378943050L).sendMessage(
                    report.getReporter().getIdLong() + ":" + e.getValue("player").getAsString() + ":" + report.getReason() + ":" + e.getValue("comments").getAsString()
                    ).addEmbeds(
                    Embed.getReportReceivedEmbed(report.getReporter(), e.getValue("player").getAsString(), report.getReason(), e.getValue("comments").getAsString()).build())
                    .addActionRow(
                            Button.primary(
                                    "report:accept",
                                    "Accept"
                            ),
                            Button.danger(
                                    "report:deny",
                                    "Deny"
                            )
                    ).queue();
        }
        else if (e.getModalId().equals("report_accept")) {

            MySQL mysql = new MySQL();
            PreparedStatement ps;
            ResultSet result;
            String[] data = e.getMessage().getContentRaw().split(":");

            try {
                Connection connection = mysql.getConnection();
                ps = connection.prepareStatement(
                        "select * from users where name = ?"
                );
                ps.setString(1, data[1]);
                result = ps.executeQuery();

                if (result.next()) {

                    // アカウントが制限されていないか
                    if (result.getInt("priv") % 2 != 0) {
                        ps = connection.prepareStatement(
                                "update users set priv = ? where id = ?"
                        );
                        ps.setInt(1, result.getInt("priv") - 1);
                        ps.setInt(2, result.getInt("id"));
                        ps.executeUpdate();
                    }

                    User user = e.getJDA().getUserById(Long.parseLong(data[0]));
                    user.openPrivateChannel()
                                    .flatMap(channel-> channel.sendMessageEmbeds(
                                            Embed.getAcceptedReportEmbed(e.getValue("comment").getAsString(), e.getMember().getEffectiveName()).build()
                                    )).queue();

                    e.reply("Report accepted.").setEphemeral(true).queue();
                    e.getMessage().addReaction(
                            Emoji.fromUnicode("U+2714")
                    ).queue();

                    e.getJDA().getTextChannelById(1194247205272424550L)
                            .sendMessageEmbeds(
                                    Embed.getRestrictEmbed(result.getInt("id"), result.getString("name"), data[2], e.getMember()).build()
                            ).queue();

                    return;
                }

                // プレイヤーが存在しない場合

                e.replyEmbeds(
                        Embed.getErrorEmbed("The player does not exist.\n" +
                                "Please request again!").build()
                ).setEphemeral(true).queue();

                e.getMessage().removeReaction(
                        Emoji.fromUnicode("U+2753")
                ).queue();

                e.getMessage().addReaction(
                        Emoji.fromUnicode("U+26A0")
                ).queue();

            } catch (SQLException ex) {
                ex.printStackTrace();
            }

        }
        else if (e.getModalId().equals("report_deny")) {

            String[] data = e.getMessage().getContentRaw().split(":");

            User user = e.getJDA().getUserById(Long.parseLong(data[0]));
            user.openPrivateChannel()
                    .flatMap(channel-> channel.sendMessageEmbeds(
                            Embed.getDeniedReportEmbed(e.getValue("comment").getAsString(), e.getMember().getEffectiveName()).build()
                    )).queue();

            e.reply("Report denied.").setEphemeral(true).queue();

            e.getMessage().addReaction(
                    Emoji.fromUnicode("U+274E")
            ).queue();
        }
    }
}

