package src.main.java.gui;

import javax.swing.*;
import javax.swing.event.ChangeListener;

import src.main.java.img.ImageResources;
import src.main.java.stream.StreamHandler;
import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

public class MediaPlayerView extends JFrame {

    private JLabel playStopButton;
    private JLabel nextButton;
    private JLabel beakButton;
    private JLabel roundButton;
    private JLabel randomButton;

    private ImageIcon playStopIcon;
    private ImageIcon roundIcon;
    private ImageIcon randomIcon;

    private JLabel trackLabel;
    private JLabel additionalLabel;
    private JLabel imageLabel;
    private JSlider volumeSlider;
    private JProgressBar progressBar;
    private ImageIcon imageIcon;
    private final Color Background = Color.DARK_GRAY; 

    public MediaPlayerView(int level) {
        imageIcon = new ImageIcon();
        playStopIcon = new ImageIcon();
        roundIcon = new ImageIcon();
        randomIcon = new ImageIcon();
        initialization(imageIcon, ImageResources.defaultImg, 300, 300);
        initialization(playStopIcon, ImageResources.playImg, 50, 50);
        initialization(roundIcon, ImageResources.roundImgOff, 25, 25);
        initialization(randomIcon, ImageResources.randomImgOff, 25, 25);

        // this.imageIcon.setImage(imageIcon.getImage().getScaledInstance(300, 300,

        // Image.SCALE_SMOOTH));

        // this.playStopIcon = new ImageIcon(System.getProperty("user.dir") +
        // "/src/main/resources/img/play.png");
        // this.playStopIcon.setImage(playStopIcon.getImage().getScaledInstance(50, 50,
        // Image.SCALE_SMOOTH));

        // this.roundIcon = new ImageIcon(System.getProperty("user.dir") +
        // "/src/main/resources/img/round.png");
        // this.roundIcon.setImage(roundIcon.getImage().getScaledInstance(25, 25,
        // Image.SCALE_SMOOTH));

        // this.randomIcon = new ImageIcon(System.getProperty("user.dir") +
        // "/src/main/resources/img/random.png");
        // this.randomIcon.setImage(randomIcon.getImage().getScaledInstance(25, 25,
        // Image.SCALE_SMOOTH));

        getContentPane().setBackground(Background);
        setTitle("Media Player");
        setSize(500, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel titlePanel = createTitlePanel();
        JPanel controlPanel = createControlPanelImg();
        JPanel imagePanel = createImagePanel();
        JPanel trackPanel = createTrackPanel();
        JPanel volumePanel = createVolumeSlider();
        JPanel progressBarPanel = createProgressBar();

        JPanel mainPanel = createMainPanel(titlePanel, controlPanel, imagePanel, trackPanel, volumePanel,
                progressBarPanel);

        add(mainPanel, BorderLayout.CENTER);
        setVolume(level);

        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                StreamHandler.stopStreams();
                System.out.println("Окно закрывается, дополнительные действия выполнены.");
            }
        });

        setVisible(true);
    }

    private void initialization(ImageIcon imageIcons, BufferedImage img, int wh, int ht) {
        imageIcons.setImage(img.getScaledInstance(wh, ht, Image.SCALE_SMOOTH));
    }

    private JPanel createVolumeSlider() {
        JPanel volumePanel = new JPanel(new BorderLayout());
        volumeSlider = new JSlider(0, 100, 50);
        volumeSlider.setMajorTickSpacing(10);
        volumeSlider.setMinorTickSpacing(5);
        volumeSlider.setBackground(Background);
        volumeSlider.setForeground(Color.WHITE);
        volumeSlider.setPreferredSize(new Dimension(200, 50));
        volumePanel.add(volumeSlider);
        return volumePanel;
    }

    private JPanel createProgressBar() {
        JPanel progressPanel = new JPanel(new BorderLayout());
        progressPanel.setBackground(Background);

        progressBar = new JProgressBar(0, 100);
        // progressBar.setPreferredSize(new Dimension(150, 6));
        progressBar.setBackground(Background);
        progressBar.setForeground(Color.WHITE);
        progressBar.setPreferredSize(new Dimension(3, 6));
        progressPanel.add(progressBar, BorderLayout.CENTER);
        return progressPanel;
    }

    private JPanel createTitlePanel() {
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
        titlePanel.setBackground(Background);
        JLabel titleLabel = new JLabel("Media Player");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);

        ImageIcon logo;
        logo = new ImageIcon(ImageResources.logo.getScaledInstance(30, 30, Image.SCALE_SMOOTH));
        JLabel jLogo = new JLabel(logo);
        titlePanel.add(jLogo);
        titlePanel.add(titleLabel);
        titlePanel.setPreferredSize(new Dimension(300, 50));
        return titlePanel;
    }

    private JPanel createControlPanelImg() {
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 35, 0));
        controlPanel.setBackground(Background);

        ImageIcon icon;
        icon = new ImageIcon(ImageResources.next.getScaledInstance(25, 25, Image.SCALE_SMOOTH));
        nextButton = new JLabel(icon);

        icon = new ImageIcon(ImageResources.back.getScaledInstance(25, 25, Image.SCALE_SMOOTH));
        beakButton = new JLabel(icon);

        playStopButton = new JLabel(playStopIcon);
        roundButton = new JLabel(roundIcon);
        randomButton = new JLabel(randomIcon);

        controlPanel.add(randomButton);
        controlPanel.add(beakButton);
        controlPanel.add(playStopButton);
        controlPanel.add(nextButton);
        controlPanel.add(roundButton);

        return controlPanel;
    }

    private JPanel createImagePanel() {
        JPanel imagePanel = new JPanel();
        imagePanel.setPreferredSize(new Dimension(300, 300));
        imagePanel.setBackground(Background);
        imagePanel.setLayout(new BorderLayout());
        // Image image = imageIcon.getImage();
        // imageIcon = new ImageIcon(image);
        imageLabel = new JLabel(imageIcon);
        imagePanel.add(imageLabel, BorderLayout.CENTER);
        return imagePanel;
    }

    private JPanel createTrackPanel() {
    JPanel createTrackPanel = new JPanel();
    createTrackPanel.setLayout(new BoxLayout(createTrackPanel, BoxLayout.Y_AXIS));
    createTrackPanel.setPreferredSize(new Dimension(300, 50));
    createTrackPanel.setBackground(Background);

    // Основной текст
    trackLabel = new JLabel("Current Track: None");
    trackLabel.setFont(new Font("Arial", Font.BOLD, 20));
    trackLabel.setForeground(Color.WHITE);
    trackLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

    // Дополнительный текст
    additionalLabel = new JLabel("No data available");
    additionalLabel.setFont(new Font("Arial", Font.PLAIN, 14));
    additionalLabel.setForeground(Color.LIGHT_GRAY);
    additionalLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

    // Добавляем метки в панель
    createTrackPanel.add(trackLabel);
    createTrackPanel.add(additionalLabel);

    return createTrackPanel;
}


    private JPanel createMainPanel(JPanel titlePanel, JPanel controlPanel, JPanel imagePanel, JPanel trackPanel,
            JPanel volumePanel, JPanel progressBarPanel) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Background);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(15, 52, 10, 52);
        gbc.gridy = 0;
        panel.add(titlePanel, gbc);

        gbc.gridy = 1;
        panel.add(imagePanel, gbc);

        gbc.insets = new Insets(10, 60, 10, 60);
        gbc.gridy = 2;
        panel.add(trackPanel, gbc);

        gbc.insets = new Insets(10, 52, 15, 52);
        gbc.gridy = 3;
        panel.add(volumePanel, gbc);

        gbc.insets = new Insets(10, 60, 15, 60);
        gbc.gridy = 4;
        panel.add(progressBarPanel, gbc);

        gbc.insets = new Insets(15, 25, 20, 25);
        gbc.gridy = 5;
        panel.add(controlPanel, gbc);

        return panel;
    }

    public void setTrackLabel(String text) {
        trackLabel.setText(text);
    }

    public void setAdditionalLabel(String text) {
        additionalLabel.setText(text);
    }

    public int getVolume() {
        return volumeSlider.getValue();
    }

    public void setImage(BufferedImage img) {
        imageIcon.setImage(img.getScaledInstance(300, 300, Image.SCALE_SMOOTH));
        imageLabel.revalidate();
        imageLabel.repaint();
    }

    public void setPlayStopIcon(BufferedImage img) {
        playStopIcon.setImage(img.getScaledInstance(50, 50, Image.SCALE_SMOOTH));
        playStopButton.revalidate();
        playStopButton.repaint();
    }

    public void setRoundIcon(BufferedImage img) {
        roundIcon.setImage(img.getScaledInstance(25, 25, Image.SCALE_SMOOTH));
        roundButton.revalidate();
        roundButton.repaint();
    }

    public void setRandomStopIcon(BufferedImage img) {
        randomIcon.setImage(img.getScaledInstance(25, 25, Image.SCALE_SMOOTH));
        randomButton.revalidate();
        randomButton.repaint();
    }

    public void setVolume(int level) {
        volumeSlider.setValue(level);
    }

    public int getProgressBar() {
        return progressBar.getValue();
    }

    public void setProgressBar(int progress) {
        progressBar.setValue(progress);
    }

    public void addVolumeChangeListener(ChangeListener listener) {
        volumeSlider.addChangeListener(listener);
    }

    public void addPlayStopClickListener(MouseListener listenForPlayStopClick) {
        playStopButton.addMouseListener(listenForPlayStopClick);
    }

    public void addBackClickListener(MouseListener listenForBackClick) {
        beakButton.addMouseListener(listenForBackClick);
    }

    public void addNextClickListener(MouseListener listenForNextClick) {
        nextButton.addMouseListener(listenForNextClick);
    }

    public void addRoundClickListener(MouseListener listenForRoundClick) {
        roundButton.addMouseListener(listenForRoundClick);
    }

    public void addRandomClickListener(MouseListener listenForRandomClick) {
        randomButton.addMouseListener(listenForRandomClick);
    }

}
