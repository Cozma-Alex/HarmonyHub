package HarmonyHub.Service.Events;

import HarmonyHub.Models.Friendship;
import HarmonyHub.Models.FriendshipRequest;

public class FriendshipChangeEvent implements Event{
    private final ChangeEventType type;
    private Friendship friendship;
    private FriendshipRequest friendshipRequest;
    private FriendshipRequest oldFriendshipRequest;

    public FriendshipChangeEvent(ChangeEventType type, FriendshipRequest friendshipRequest, FriendshipRequest oldFriendshipRequest) {
        this.type = type;
        this.friendshipRequest = friendshipRequest;
        this.oldFriendshipRequest = oldFriendshipRequest;
    }

    public FriendshipChangeEvent(ChangeEventType type, FriendshipRequest friendshipRequest) {
        this.type = type;
        this.friendshipRequest = friendshipRequest;
    }

    public FriendshipChangeEvent(ChangeEventType type, Friendship friendship) {
        this.type = type;
        this.friendship = friendship;
    }

    public ChangeEventType getType() {
        return type;
    }

    public Friendship getFriendship() {
        return friendship;
    }

    public FriendshipRequest getFriendshipRequest() {
        return friendshipRequest;
    }

    public FriendshipRequest getOldFriendshipRequest() {
        return oldFriendshipRequest;
    }
}
