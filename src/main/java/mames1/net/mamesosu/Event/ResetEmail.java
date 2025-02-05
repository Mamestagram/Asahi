package mames1.net.mamesosu.Event;

import mames1.net.mamesosu.Main;
import mames1.net.mamesosu.Object.Ticket;
import mames1.net.mamesosu.Utils.Embed;
import mames1.net.mamesosu.Utils.Modal;
import mames1.net.mamesosu.Object.MySQL;
import mames1.net.mamesosu.Utils.Password;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;

import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ResetEmail extends ListenerAdapter {

    @Override
    public void onStringSelectInteraction(StringSelectInteractionEvent e) {
        if (e.getComponentId().equals("menu:dropdown")) {
            if (e.getSelectedOptions().get(0).getValue().equals("change_email")) {
                TextInput userName = Modal.createTextInput(
                        "username",
                        "Username",
                        "peppy",
                        true,
                        TextInputStyle.SHORT
                );
                TextInput password = Modal.createTextInput(
                        "password",
                        "Password",
                        "osu!123",
                        true,
                        TextInputStyle.SHORT
                );

                net.dv8tion.jda.api.interactions.modals.Modal modal = net.dv8tion.jda.api.interactions.modals.Modal.create(
                        "change_email_form",
                        "Please enter your account information."
                ).addActionRows(
                        ActionRow.of(userName),
                        ActionRow.of(password)
                ).build();

                e.replyModal(modal).queue();
            }
        }
    }

    @Override
    public void onModalInteraction(ModalInteractionEvent e) {

        MySQL mysql = new MySQL();
        PreparedStatement ps;
        ResultSet result;

        if (e.getModalId().equals("change_email_form")) {
            try {
                Connection connection = mysql.getConnection();
                ps = connection.prepareStatement("select pw_bcrypt from users where name = ?");
                ps.setString(1, e.getValue("username").getAsString());
                result = ps.executeQuery();

                // usernameが一致している場合
                if(result.next()) {
                    try {
                        // パスワードが一致している場合
                        if (Password.checkPassword(e.getValue("password").getAsString(), result.getString("pw_bcrypt"))) {
                            e.getMessage().editMessageEmbeds(
                                    Embed.getApprovedEmailChangeRequest().build()
                            ).setComponents(ActionRow.of(
                                            Button.success(
                                                    "change_email",
                                                    "Change Email"
                                            )
                                    )
                            ).queue();

                            e.reply(
                                    "The request has been approved."
                            ).setEphemeral(true).queue();
                            Main.tickets.add(new Ticket(Integer.parseInt(e.getChannel().getName().replace("ticket-", "")) , null, e.getValue("username").getAsString(), e.getValue("password").getAsString()));
                            return;
                        }
                    } catch (NoSuchAlgorithmException ex) {
                        ex.printStackTrace();
                        e.replyEmbeds(Embed.getErrorEmbed(
                                "An error occurred while processing the password. Please try again."
                        ).build()).setEphemeral(true).queue();
                    }
                }

                e.replyEmbeds(Embed.getErrorEmbed(
                        "The process was interrupted because the username or password is incorrect. Please enter the correct information."
                ).build()).setEphemeral(true).queue();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else if (e.getModalId().equals("change_email")) {
            // メールアドレスの変更処理
            try {
                Ticket ticket = Main.tickets.stream().filter(t -> t.getId() == Integer.parseInt(e.getChannel().getName().replace("ticket-", ""))).findFirst().orElse(null);
                Connection connection = mysql.getConnection();
                ps = connection.prepareStatement("update users set email = ? where name = ?");
                ps.setString(1, e.getValue("email").getAsString());
                ps.setString(2, ticket.getUsername());
                ps.executeUpdate();

                e.reply("The email has been changed.").setEphemeral(true).queue();

                e.getMessage().editMessageEmbeds(
                        Embed.getChangedEmailEmbed(e.getValue("email").getAsString()).build()
                ).setComponents().queue();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent e) {

        if (e.getComponentId().equals("change_email")) {
                TextInput email = Modal.createTextInput(
                        "email",
                        "Email",
                        "example@osu.jp",
                        true,
                        TextInputStyle.SHORT
                );
                net.dv8tion.jda.api.interactions.modals.Modal modal = net.dv8tion.jda.api.interactions.modals.Modal.create(
                        "change_email",
                        "Please enter your new email."
                ).addActionRows(
                        ActionRow.of(email)
                ).build();
                e.replyModal(modal).queue();
        }
    }

}
