package HarmonyHub.Models;

import java.util.Objects;
import java.util.UUID;

public class ChatTheme extends Entity<UUID> {

    private String themeName;
    private String backgroundModel;
    private String userColor;
    private String otherColor;

    @Override
    public String toString() {
        return "ChatTheme{" +
                "themeName='" + themeName + '\'' +
                ", backgroundModel='" + backgroundModel + '\'' +
                ", userColor='" + userColor + '\'' +
                ", otherColor='" + otherColor + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ChatTheme chatTheme = (ChatTheme) o;
        return Objects.equals(themeName, chatTheme.themeName) && Objects.equals(backgroundModel, chatTheme.backgroundModel) && Objects.equals(userColor, chatTheme.userColor) && Objects.equals(otherColor, chatTheme.otherColor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), themeName, backgroundModel, userColor, otherColor);
    }

    public String getThemeName() {
        return themeName;
    }

    public void setThemeName(String themeName) {
        this.themeName = themeName;
    }

    public String getBackgroundModel() {
        return backgroundModel;
    }

    public void setBackgroundModel(String backgroundModel) {
        this.backgroundModel = backgroundModel;
    }

    public String getUserColor() {
        return userColor;
    }

    public void setUserColor(String userColor) {
        this.userColor = userColor;
    }

    public String getOtherColor() {
        return otherColor;
    }

    public void setOtherColor(String otherColor) {
        this.otherColor = otherColor;
    }
}
