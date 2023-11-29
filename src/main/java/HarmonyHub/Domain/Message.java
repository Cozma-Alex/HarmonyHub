package HarmonyHub.Domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;

public class Message extends Entity<String>{
    private String message;
    private User from;
    private ArrayList<User> receivers;
    private LocalDateTime messageDate;

    private Message reply;

    public Message getReply() {
        return reply;
    }

    public void setReply(Message reply) {
        this.reply = reply;
    }

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy-hh:mm");

    public Message(String message, User from, ArrayList<User> receivers) {
        this.message = message;
        this.from = from;
        this.receivers = receivers;
        messageDate = LocalDateTime.now();
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("Message: from(" + this.from + ") to( ");
        for ( User rev : this.receivers) {
            str.append(rev).append(" ");
        }
        str.append(") Date(").append(dateTimeFormatter.format(messageDate)).append(")");
        return str.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Message message1 = (Message) o;
        return Objects.equals(message, message1.message) && Objects.equals(from, message1.from) && Objects.equals(receivers, message1.receivers) && Objects.equals(messageDate, message1.messageDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), message, from, receivers, messageDate);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getFrom() {
        return from;
    }

    public void setFrom(User from) {
        this.from = from;
    }

    public ArrayList<User> getReceivers() {
        return receivers;
    }

    public void setReceivers(ArrayList<User> receivers) {
        this.receivers = receivers;
    }

    public LocalDateTime getMessageDate() {
        return messageDate;
    }

    public void setMessageDate(LocalDateTime messageDate) {
        this.messageDate = messageDate;
    }
}
