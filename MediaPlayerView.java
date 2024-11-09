import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionListener;

public class MediaPlayerView extends JFrame {
    // Кнопки управления
    private JButton playStopButton; // Объединенная кнопка "Play"/"Stop"
    private JButton backButton; // Кнопка для перехода на предыдущий трек
    private JButton nextButton; // Кнопка для перехода на следующий трек

    // private JProgressBar progressBar;
    private JLabel trackLabel; // Метка для отображения текущего трека
    private JSlider volumeSlider;
    private JSlider slider;
    // Ползунок для громкости

    public MediaPlayerView(int volume) {
        // Настройки окна
        getContentPane().setBackground(Color.DARK_GRAY);
        setTitle("Media Player");
        setSize(500, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Панель для заголовка
        JPanel titlePanel = createTitlePanel();

        // Панель для кнопок управления
        JPanel controlPanel = createControlPanel();

        // Панель с изображением и меткой трека
        JPanel imagePanel = createImagePanel();

        JPanel trackPanel = createTrackPanel();
        // Панель с элементами управления (сетка)
        JPanel panel = createMainPanel(titlePanel, controlPanel, imagePanel,trackPanel);

        
        // Размещение панели с элементами управления в окно
        add(panel, BorderLayout.NORTH);

        // Отображение окна
        setVisible(true);
    }

    // private JPanel createProgressBar(){
    // // Создаем строку для прогресса
    // JPanel progressPanel = new JPanel(new BorderLayout());
    // progressPanel.setBackground(Color.DARK_GRAY);

    // progressBar = new JProgressBar(0, 100);
    // progressBar.setPreferredSize(new Dimension(300, 25));
    // progressBar.setBackground(Color.GRAY);
    // progressBar.setForeground(Color.GREEN);
    // progressPanel.add(progressBar, BorderLayout.CENTER);
    // return progressPanel;
    // }

    // Создание панели для заголовка
    private JPanel createTitlePanel() {
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(Color.DARK_GRAY);
        JLabel titleLabel = new JLabel("My Media Player");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        titlePanel.setPreferredSize(new Dimension(300, 50));
        return titlePanel;
    }
    
    // Создание панели для кнопок управления
    private JPanel createControlPanel() {
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());
        controlPanel.setBackground(Color.DARK_GRAY);

        playStopButton = new JButton("Play");
        backButton = new JButton("Back");
        nextButton = new JButton("Next");

        controlPanel.add(backButton);
        controlPanel.add(playStopButton);
        controlPanel.add(nextButton);

        playStopButton.setPreferredSize(new Dimension(100, 40));
        backButton.setPreferredSize(new Dimension(100, 40));
        nextButton.setPreferredSize(new Dimension(100, 40));

        return controlPanel;
    }

    // Создание панели для изображения
    private JPanel createImagePanel() {
        JPanel imagePanel = new JPanel();
        imagePanel.setPreferredSize(new Dimension(300, 300));
        imagePanel.setBackground(Color.DARK_GRAY);
        imagePanel.setLayout(new BorderLayout());
        String imagePath = "/home/ars/Documents/Code development/Java/player/extracted_cover.jpg";
        ImageIcon imageIcon = new ImageIcon(imagePath);

        // Get the image from ImageIcon
        Image image = imageIcon.getImage();

        // Scale the image to a fixed size (e.g., 300x300)
        image = image.getScaledInstance(300, 300, Image.SCALE_SMOOTH);

        // Update the ImageIcon with the new scaled image
        imageIcon = new ImageIcon(image);

        // Create JLabel and set the ImageIcon
        JLabel imageLabel = new JLabel(imageIcon);

        // Add JLabel with the image to the JPanel
        imagePanel.add(imageLabel, BorderLayout.CENTER);

        return imagePanel;
    }

    private JPanel createTrackPanel() {
        JPanel createTrackPanel = new JPanel(new BorderLayout());
        createTrackPanel.setPreferredSize(new Dimension(300, 50));
        createTrackPanel.setBackground(Color.DARK_GRAY);
        trackLabel = new JLabel("Current Track: None");
        trackLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        trackLabel.setHorizontalAlignment(SwingConstants.CENTER);
        trackLabel.setForeground(Color.WHITE);
        trackLabel.setVerticalAlignment(SwingConstants.TOP);
        trackLabel.setPreferredSize(new Dimension(300, 50));
        createTrackPanel.add(trackLabel, BorderLayout.NORTH);
        return createTrackPanel;
    }

    // Создание главной панели с элементами управления
    private JPanel createMainPanel(JPanel titlePanel, JPanel controlPanel, JPanel imagePanel , JPanel trackPanel) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.DARK_GRAY);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 25, 10, 25);

        // Ползунок громкости
        volumeSlider = new JSlider(0, 100, 50);
        volumeSlider.setMajorTickSpacing(10);
        volumeSlider.setMinorTickSpacing(5);
        volumeSlider.setBackground(Color.DARK_GRAY);
        volumeSlider.setPreferredSize(new Dimension(200, 50));

        slider = new JSlider(0, 100, 0);
        slider.setMajorTickSpacing(10);
        slider.setMinorTickSpacing(5);
        slider.setBackground(Color.DARK_GRAY);
        slider.setPreferredSize(new Dimension(300, 50));

        // Размещение компонентов на панели
        gbc.gridwidth = 2;
        panel.add(titlePanel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        panel.add(imagePanel, gbc);

        gbc.gridy = 2;
        panel.add(trackPanel, gbc);

        gbc.gridy = 3;
        panel.add(volumeSlider, gbc);

        gbc.gridy = 4;
        panel.add(slider, gbc);

        gbc.gridy = 5;
        panel.add(controlPanel, gbc);

        return panel;
    }

    // Методы для обновления информации на экране и получения значений
    public void setTrackLabel(String text) {
        trackLabel.setText(text);
    }

    public int getSlider() {
        return slider.getValue();
    }

    public void setSlider(int progress) {
        slider.setValue(progress);
    }

    public int getVolume() {
        return volumeSlider.getValue();
    }

    public void setImage(byte[] img) {

    }

    // Методы для добавления слушателей
    public void addVolumeChangeListener(ChangeListener listener) {
        volumeSlider.addChangeListener(listener);
    }

    public void addPlayStopListener(ActionListener listenForPlayStopButton) {
        playStopButton.addActionListener(listenForPlayStopButton);
    }

    public void addBackListener(ActionListener listenForBackButton) {
        backButton.addActionListener(listenForBackButton);
    }

    public void addNextListener(ActionListener listenForNextButton) {
        nextButton.addActionListener(listenForNextButton);
    }

    // Метод для изменения текста на кнопке Play/Stop
    public void setPlayStopButtonText(String text) {
        playStopButton.setText(text);
    }
}
