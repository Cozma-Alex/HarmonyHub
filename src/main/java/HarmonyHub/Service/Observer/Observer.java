package HarmonyHub.Service.Observer;


import HarmonyHub.Service.Events.Event;

public interface Observer<T extends Event>{

    void update(T event);
}
