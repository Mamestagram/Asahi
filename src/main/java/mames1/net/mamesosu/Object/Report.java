package mames1.net.mamesosu.Object;

import net.dv8tion.jda.api.entities.Member;

public class Report {

    private Member reporter;
    private String reason;
    private String description;

    public Report(Member reporter, String reason, String description) {
        this.reporter = reporter;
        this.reason = reason;
        this.description = description;
    }

    public String getReason() {
        return reason;
    }

    public Member getReporter() {
        return reporter;
    }
}
