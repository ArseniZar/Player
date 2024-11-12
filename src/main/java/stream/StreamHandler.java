package src.main.java.stream;
import src.main.java.interfaces.Action;
public class StreamHandler {
    private static boolean isGood = true;

    public static <T> void startStream(Action<T> action) {
        if (isGood) {
            new Thread(() -> {
                action.execute(null);
            }).start();
        }
    }

    public static <T> void startStreamWithWhile(Action<T> action , boolean index) {
        if (isGood) {
            new Thread(() -> {
                while (isGood && index) {

                    action.execute(null);
                }
            }).start();
        }
    }

    public static void stopStreams() {
        isGood = false;
    }


    
}
