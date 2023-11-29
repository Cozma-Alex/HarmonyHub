package HarmonyHub.GUI.Observer;

import HarmonyHub.GUI.Events.Event;

public interface Observable<T extends Event>{
    void addObserver(Observer<T> observer);

    void removeObserver(Observer<T> observer);

    void notify(T event);
}
