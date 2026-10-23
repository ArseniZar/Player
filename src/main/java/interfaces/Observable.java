package src.main.java.interfaces;

import java.util.function.Consumer;

public interface Observable<T> {
    void addObserver(T observer);

    void removeObserver(T observer);

    void notifyObservers(Consumer<T> action);

}
