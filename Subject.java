import observer.*;

public interface Subject {
    void addObserver1(ObserverTreck observer);
    void removeObserver1(ObserverTreck observer);
    void notifyObservers1(String message);

    
    void addObserver2(ObserverPlay observer);
    void removeObserver2(ObserverPlay observer);
    void notifyObservers2(String message);
}
