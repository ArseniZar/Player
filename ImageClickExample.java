import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ImageClickExample extends JFrame {

    public ImageClickExample() {
        setTitle("Image Click Example");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        // Замените путь к изображению на ваш файл
        String nextTrackImagePath = "/home/ars/Documents/Progect/player/src/main/resources/img/free-icon-play-buttton-5198450.png";
        String previousTrackImagePath = "previous_track.png";

        // Создаем метку с изображением "Следующий трек" и добавляем действие на клик
        JLabel nextTrackLabel = createClickableImageLabel(nextTrackImagePath, "Следующий трек");

        // Создаем метку с изображением "Предыдущий трек" и добавляем действие на клик
        JLabel previousTrackLabel = createClickableImageLabel(previousTrackImagePath, "Предыдущий трек");

        // Добавляем метки в окно
        add(previousTrackLabel);
        add(nextTrackLabel);
    }

    // Метод для создания метки с изображением и обработкой клика
    private JLabel createClickableImageLabel(String imagePath, String message) {
        // Загружаем изображение
        ImageIcon icon = new ImageIcon(imagePath);

        // Создаем метку с изображением
        JLabel label = new JLabel(icon);

        // Добавляем слушатель кликов
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(null, message);
            }
        });

        return label;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ImageClickExample example = new ImageClickExample();
            example.setVisible(true);
        });
    }
}
