package HarmonyHub.Domain;

import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

import java.time.LocalDateTime;

public class FriendshipRequest extends Entity<Pair<String, String>> {
    private LocalDateTime dateRequest;
    private String status;

    private String firstNameFrom;
    private String firstNameTo;
    private String LastNameFrom;
    private String LastNameTo;
    private String emailFrom;
    private String emailTo;

    public String getFirstNameFrom() {
        return firstNameFrom;
    }

    public void setFirstNameFrom(String firstNameFrom) {
        this.firstNameFrom = firstNameFrom;
    }

    public String getFirstNameTo() {
        return firstNameTo;
    }

    public void setFirstNameTo(String firstNameTo) {
        this.firstNameTo = firstNameTo;
    }

    public String getLastNameFrom() {
        return LastNameFrom;
    }

    public void setLastNameFrom(String lastNameFrom) {
        LastNameFrom = lastNameFrom;
    }

    public String getLastNameTo() {
        return LastNameTo;
    }

    public void setLastNameTo(String lastNameTo) {
        LastNameTo = lastNameTo;
    }

    public String getEmailFrom() {
        return emailFrom;
    }

    public void setEmailFrom(String emailFrom) {
        this.emailFrom = emailFrom;
    }

    public String getEmailTo() {
        return emailTo;
    }

    public void setEmailTo(String emailTo) {
        this.emailTo = emailTo;
    }

    private final SimpleObjectProperty<Button> acceptButton = new SimpleObjectProperty<>(new Button("Accept"));
    private final SimpleObjectProperty<Button> denyButton = new SimpleObjectProperty<>(new Button("Deny"));

    public FriendshipRequest(LocalDateTime date ,String status, EventHandler<ActionEvent> acceptHandler, EventHandler<ActionEvent> denyHandler) {
        this.status = status;
        this.dateRequest = date;

        acceptButton.get().setOnAction(acceptHandler);
        denyButton.get().setOnAction(denyHandler);
    }

    public Button getAcceptButton() {
        return acceptButton.get();
    }

    public SimpleObjectProperty<Button> acceptButtonProperty() {
        return acceptButton;
    }

    public Button getDenyButton() {
        return denyButton.get();
    }

    public SimpleObjectProperty<Button> denyButtonProperty() {
        return denyButton;
    }
    public LocalDateTime getDateRequest() {
        return dateRequest;
    }

    public void setDateRequest(LocalDateTime dateRequest) {
        this.dateRequest = dateRequest;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
