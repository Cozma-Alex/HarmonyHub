package HarmonyHub.Models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class Message extends Entity<UUID> {

    private UUID idChat;
    private UUID idFrom;
    private UUID idReply;
    private String message;
    private LocalDateTime date;
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy-hh:mm");
    private final ArrayList<UUID> receivers = new ArrayList<>();

    public ArrayList<UUID> getReceivers() {
        return receivers;
    }

    public void addOneReceiver(UUID receiver){
        receivers.add(receiver);
    }

    public Message(UUID idChat, UUID idFrom, String message, LocalDateTime date) {
        this.idChat = idChat;
        this.idFrom = idFrom;
        this.message = message;
        this.date = date;
    }

    public Message(UUID idChat, UUID idFrom, UUID idReply, String message, LocalDateTime date) {
        this.idChat = idChat;
        this.idFrom = idFrom;
        this.idReply = idReply;
        this.message = message;
        this.date = date;
    }

    public UUID getIdChat() {
        return idChat;
    }

    public void setIdChat(UUID idChat) {
        this.idChat = idChat;
    }

    public UUID getIdFrom() {
        return idFrom;
    }

    public void setIdFrom(UUID idFrom) {
        this.idFrom = idFrom;
    }

    public UUID getIdReply() {
        return idReply;
    }

    public void setIdReply(UUID idReply) {
        this.idReply = idReply;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Message{" +
                "idChat=" + idChat +
                ", idFrom=" + idFrom +
                ", idReply=" + idReply +
                ", message='" + message + '\'' +
                ", date=" + date +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Message message1 = (Message) o;
        return Objects.equals(idChat, message1.idChat) && Objects.equals(idFrom, message1.idFrom) && Objects.equals(idReply, message1.idReply) && Objects.equals(message, message1.message) && Objects.equals(date, message1.date) && Objects.equals(dateTimeFormatter, message1.dateTimeFormatter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), idChat, idFrom, idReply, message, date, dateTimeFormatter);
    }
}
