package HarmonyHub.Service.Observer;


import HarmonyHub.Service.Events.Event;

public interface Observable<T extends Event>{
    void addObserver(Observer<T> observer);

    void removeObserver(Observer<T> observer);

    void notify(T event);
}
