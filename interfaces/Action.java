package interfaces;

@FunctionalInterface
public interface Action<T> {
    void execute(T observer); 
}
