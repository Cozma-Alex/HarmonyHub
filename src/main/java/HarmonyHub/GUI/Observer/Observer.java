package HarmonyHub.GUI.Observer;

import HarmonyHub.GUI.Events.Event;

public interface Observer<T extends Event>{

    void update(T event);
}
