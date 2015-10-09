package co.appsnik.chuck;

import java.util.Observable;

class ConnectivityChangeObservable extends Observable {
    private static final ConnectivityChangeObservable instance = new ConnectivityChangeObservable();

    public static ConnectivityChangeObservable instance() {
        return instance;
    }

    private ConnectivityChangeObservable() {}

    public void notify(Boolean connected) {
        setChanged();
        notifyObservers(connected);
    }
}