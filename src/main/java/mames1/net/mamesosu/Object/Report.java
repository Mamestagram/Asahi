package mames1.net.mamesosu.Object;

import lombok.Getter;
import net.dv8tion.jda.api.entities.Member;

public class Report {

    @Getter
    private Member reporter;
    @Getter
    private String reason;
    private String description;

    public Report(Member reporter, String reason, String description) {
        this.reporter = reporter;
        this.reason = reason;
        this.description = description;
    }

}
