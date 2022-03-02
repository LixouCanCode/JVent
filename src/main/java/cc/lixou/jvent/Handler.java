package cc.lixou.jvent;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicReference;

public class Handler {

    private final TreeMap<Integer,ArrayList<Listener>> listeners;

    public Handler() {
        listeners = new TreeMap<>();
    }

    public <U extends JVent> U call(U event) {
        AtomicReference<U> result = new AtomicReference<>(event);
        for (int key : listeners.keySet()) {
            listeners.get(key).forEach((listener -> result.set(listener.call(result.get()))));
        }
        return result.get();
    }

    public void subscribe(Listener listener, EventPriority priority) {
        ArrayList<Listener> listenerz = getListeners(priority);
        if(listenerz.contains(listener)) { return; }
        listenerz.add(listener);
    }

    public void unsubscribe(Listener listener, EventPriority priority) {
        ArrayList<Listener> listenerz = getListeners(priority);
        if(!listenerz.contains(listener)) { return; }
        listenerz.remove(listener);
    }

    private ArrayList<Listener> getListeners(EventPriority priority) {
        if(!listeners.containsKey(priority.ordinal())) {
            listeners.put(priority.ordinal(), new ArrayList<>());
        }
        return listeners.get(priority.ordinal());
    }

}
