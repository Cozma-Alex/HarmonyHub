package HarmonyHub.Models;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class Chat extends Entity<UUID> {

    private String chatName;
    private UUID idTheme;
    private final ArrayList<UUID> users = new ArrayList<>();

    public ArrayList<UUID> getUsers() {
        return users;
    }

    public void addOneUser(UUID user){
        users.add(user);
    }

    @Override
    public String toString() {
        return "Chat{" +
                "chatName='" + chatName + '\'' +
                ", idTheme=" + idTheme +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Chat chat = (Chat) o;
        return Objects.equals(chatName, chat.chatName) && Objects.equals(idTheme, chat.idTheme);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), chatName, idTheme);
    }

    public String getChatName() {
        return chatName;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
    }

    public UUID getIdTheme() {
        return idTheme;
    }

    public void setIdTheme(UUID idTheme) {
        this.idTheme = idTheme;
    }

    public Chat(String chatName, UUID idTheme) {
        this.chatName = chatName;
        this.idTheme = idTheme;
    }
}
