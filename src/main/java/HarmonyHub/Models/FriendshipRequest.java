package HarmonyHub.Models;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class FriendshipRequest extends Entity<UUID> {
    private UUID userSender;
    private UUID userReceiver;
    private LocalDateTime date;
    private String status;

    public FriendshipRequest(UUID userSender, UUID userReceiver, LocalDateTime date, String status) {
        this.userSender = userSender;
        this.userReceiver = userReceiver;
        this.date = date;
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        FriendshipRequest that = (FriendshipRequest) o;
        return Objects.equals(userSender, that.userSender) && Objects.equals(userReceiver, that.userReceiver) && Objects.equals(date, that.date) && Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), userSender, userReceiver, date, status);
    }

    @Override
    public String toString() {
        return "FriendshipRequest{" +
                "userSender=" + userSender +
                ", userReceiver=" + userReceiver +
                ", date=" + date +
                ", status='" + status + '\'' +
                '}';
    }

    public UUID getUserSender() {
        return userSender;
    }

    public void setUserSender(UUID userSender) {
        this.userSender = userSender;
    }

    public UUID getUserReceiver() {
        return userReceiver;
    }

    public void setUserReceiver(UUID userReceiver) {
        this.userReceiver = userReceiver;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
