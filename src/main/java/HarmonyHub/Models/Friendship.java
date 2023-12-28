package HarmonyHub.Models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.UUID;

public class Friendship extends Entity<Pair<UUID, UUID>> {

    private LocalDateTime dateFriendship;
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy-hh:mm");

    public Friendship(){}

    public void setDateFriendship(LocalDateTime dateFriendship) {
        this.dateFriendship = dateFriendship;
    }

    public LocalDateTime getDateFriendship() {
        return dateFriendship;
    }

    @Override
    public String toString() {
        return this.getId().getLeft() + "|" + this.getId().getRight() + "|" + dateTimeFormatter.format(dateFriendship);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Friendship that = (Friendship) o;
        return Objects.equals(dateFriendship, that.dateFriendship) && Objects.equals(dateTimeFormatter, that.dateTimeFormatter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), dateFriendship, dateTimeFormatter);
    }
}
