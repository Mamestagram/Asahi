package mames1.net.mamesosu.Object;

import lombok.Getter;
import lombok.Setter;

public class Ticket {

    @Getter
    int id;
    @Getter
    @Setter
    String email;
    @Getter
    String username;
    String password;

    public Ticket(int id, String email, String username, String password) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.password = password;
    }

}
