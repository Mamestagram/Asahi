package mames1.net.mamesosu.Event;

import mames1.net.mamesosu.Utils.Modal;
import mames1.net.mamesosu.Object.MySQL;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;

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
                        ActionRow.of(userName, password)
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

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}
