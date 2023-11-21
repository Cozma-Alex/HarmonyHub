package group.socialapp.GUI.Observer;

import group.socialapp.GUI.Events.Event;

public interface Observer<T extends Event>{

    void update(T event);
}
