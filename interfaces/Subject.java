package interfaces;

public interface Subject<T,N> {
    void addObserver(T observer);
    void removeObserver(T observer);
    void notifyObservers(N action);

}
