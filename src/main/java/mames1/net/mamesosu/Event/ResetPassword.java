package mames1.net.mamesosu.Event;

import mames1.net.mamesosu.Main;
import mames1.net.mamesosu.Object.MySQL;
import mames1.net.mamesosu.Object.Ticket;
import mames1.net.mamesosu.Support.Mail;
import mames1.net.mamesosu.Utils.Embed;
import mames1.net.mamesosu.Utils.Modal;
import mames1.net.mamesosu.Utils.Password;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ResetPassword extends ListenerAdapter {

    @Override
    public void onStringSelectInteraction(StringSelectInteractionEvent e) {
        if (e.getComponentId().equals("menu:dropdown")) {
            System.out.println(e.getSelectedOptions().get(0).getValue());
            if (e.getSelectedOptions().get(0).getValue().equals("change_password")) {
                TextInput email = Modal.createTextInput(
                        "email",
                        "Email",
                        "example@osu.jp",
                        true,
                        TextInputStyle.SHORT
                );
                net.dv8tion.jda.api.interactions.modals.Modal modal = net.dv8tion.jda.api.interactions.modals.Modal.create(
                        "change_password_form",
                        "Enter your email."
                ).addActionRows(
                        ActionRow.of(email)
                ).build();

                e.replyModal(modal).queue();
            }
        }
    }

    @Override
    public void onModalInteraction(ModalInteractionEvent e) {
        if (e.getModalId().equals("change_password_form")) {
            MySQL mysql = new MySQL();
            PreparedStatement ps;
            ResultSet result;

            try {
                Connection connection = mysql.getConnection();
                String email = e.getValue("email").getAsString();

                ps = connection.prepareStatement("select name, id from users where email = ?");
                ps.setString(1, email);
                result = ps.executeQuery();

                if(result.next()) {

                    Mail.sendVerificationMail(email, result.getString("name"));

                    e.getMessage().editMessageEmbeds(
                            Embed.getVerificationEmailEmbed(e.getValue("email").getAsString()).build()
                    ).setComponents(
                            ActionRow.of(
                                    Button.primary(
                                            "verify",
                                            "Enter Verification Code"
                                    )
                            )
                    ).queue();

                    e.reply("The request has been approved.").setEphemeral(true).queue();

                    Main.tickets.add(new Ticket(Integer.parseInt(e.getChannel().getName().replace("ticket-", "")), e.getValue("email").getAsString(), result.getString("name"), null));
                    return;
                }

                e.replyEmbeds(Embed.getErrorEmbed(
                        "The email you entered does not exist in our database."
                ).build()).setEphemeral(true).queue();

            } catch (SQLException ex) {

                ex.printStackTrace();

                e.replyEmbeds(Embed.getErrorEmbed(
                        "An error occurred while processing your request. Please try again later."
                ).build()).setEphemeral(true).queue();

            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

        } else if(e.getModalId().equals("verify_form")) {
            MySQL mySQL = new MySQL();
            PreparedStatement ps;
            ResultSet result;

            try {
                Connection connection = mySQL.getConnection();
                Ticket ticket = Main.tickets.stream().filter(t -> t.getId() == Integer.parseInt(e.getChannel().getName().replace("ticket-", ""))).findFirst().orElse(null);
                ps = connection.prepareStatement("select verification_code from users where email = ?");

                if (ticket == null) {
                    e.getMessage().editMessageEmbeds(
                            Embed.getErrorEmbed(
                            "The process was interrupted because the cache was reset.\n" +
                                    "We apologize for the inconvenience, but please create a new ticket and try again."
                    ).build()
                    ).setComponents().queue();

                    e.reply("Unfortunately, the process was interrupted.").setEphemeral(true).queue();
                    return;
                }

                ps.setString(1, ticket.getEmail());
                result = ps.executeQuery();

                if(result.next()) {
                    if(e.getValue("code").getAsString().equals(result.getString("verification_code"))) {

                        e.getMessage().editMessageEmbeds(
                                Embed.getApprovedPasswordChangeRequest().build()
                        ).setComponents(ActionRow.of(
                                Button.primary(
                                        "change_password",
                                        "Change Password"
                                )
                        )).queue();

                        e.reply("The request has been approved.").setEphemeral(true).queue();
                        return;
                    }
                }

                e.replyEmbeds(Embed.getErrorEmbed(
                        "The verification code you entered is incorrect."
                ).build()).setEphemeral(true).queue();
            } catch (SQLException ex) {
                ex.printStackTrace();
                e.replyEmbeds(Embed.getErrorEmbed(
                        "An error occurred while processing your request. Please try again later."
                ).build()).setEphemeral(true).queue();
            }
        } else if (e.getModalId().equals("change_password_form_1")) {
            MySQL mysql = new MySQL();
            PreparedStatement ps;

            try {
                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                Ticket ticket = Main.tickets.stream().filter(t -> t.getId() == Integer.parseInt(e.getChannel().getName().replace("ticket-", ""))).findFirst().orElse(null);
                Connection connection = mysql.getConnection();
                String password = e.getValue("password").getAsString();
                String hashedPassword = encoder.encode(Password.getHashPassword(password));

                if(ticket == null) {

                    e.getMessage().editMessageEmbeds(
                            Embed.getErrorEmbed(
                            "The process was interrupted because the cache was reset.\n" +
                                    "We apologize for the inconvenience, but please create a new ticket and try again."
                    ).build()
                    ).setComponents().queue();

                    e.reply("Unfortunately, the process was interrupted.").setEphemeral(true).queue();
                    return;
                }

                ps = connection.prepareStatement("update users set pw_bcrypt = ? where email = ?");
                // パスワードをbcryptでハッシュ化するコード書く (ここから)
                ps.setString(1, hashedPassword);
                ps.setString(2, ticket.getEmail());
                ps.executeUpdate();

                e.getMessage().editMessageEmbeds(
                        Embed.getChangedPasswordEmbed().build()
                ).setComponents().queue();

                e.reply("The password has been changed.").setEphemeral(true).queue();



            } catch (SQLException ex) {
                ex.printStackTrace();
                e.replyEmbeds(Embed.getErrorEmbed(
                        "An error occurred while processing your request. Please try again later."
                ).build()).setEphemeral(true).queue();
            } catch (NoSuchAlgorithmException ex) {
                e.replyEmbeds(Embed.getErrorEmbed(
                        "An error occurred while processing your request. Please try again later."
                ).build()).setEphemeral(true).queue();
                throw new RuntimeException(ex);
            }
        }
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent e) {

        if(e.getComponentId().equals("verify")) {
            TextInput code = Modal.createTextInput(
                    "code",
                    "Verification Code",
                    "qlHphGue6C",
                    true,
                    TextInputStyle.SHORT
            );
            net.dv8tion.jda.api.interactions.modals.Modal modal = net.dv8tion.jda.api.interactions.modals.Modal.create(
                    "verify_form",
                    "Enter your code!"
            ).addActionRows(
                    ActionRow.of(code)
            ).build();

            e.replyModal(modal).queue();
        } else if(e.getComponentId().equals("change_password")) {
            TextInput password = Modal.createTextInput(
                    "password",
                    "Password",
                    "osu!123",
                    true,
                    TextInputStyle.SHORT
            );
            net.dv8tion.jda.api.interactions.modals.Modal modal = net.dv8tion.jda.api.interactions.modals.Modal.create(
                    "change_password_form_1",
                    "Enter your new password."
            ).addActionRows(
                    ActionRow.of(password)
            ).build();

            e.replyModal(modal).queue();
        }
    }
}
