package group.socialapp.GUI.Events;

import group.socialapp.Domain.User;

public class UserChangeEvent implements Event {

    private final ChangeEventType type;

    private final User user;
    private User oldUser;

    public UserChangeEvent(ChangeEventType type, User user) {
        this.type = type;
        this.user = user;
    }

    public UserChangeEvent(ChangeEventType type, User user, User oldUser) {
        this.type = type;
        this.user = user;
        this.oldUser = oldUser;
    }

    public ChangeEventType getType(){return type;}

    public User getUser(){return user;}

    public User getOldUser(){return oldUser;}

}
