package HarmonyHub.Domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Friendship extends Entity<Pair<String,String>> {

    private LocalDateTime date;

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy-hh:mm");

    public Friendship() {}

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public LocalDateTime getDate() {
        return date;
    }

    @Override
    public String toString() {
        return this.getId().getLeft() + "|" + this.getId().getRight() + "|" + dateTimeFormatter.format(date);
    }
}
