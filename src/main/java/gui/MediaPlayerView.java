package src.main.java.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSlider;

import src.main.java.img.ImageResources;
import src.main.java.interfaces.Observable;
import src.main.java.interfaces.observer.MediaPlayerUIEventsObserver;
import src.main.java.stream.StreamHandler;

public class MediaPlayerView extends JFrame implements Observable<MediaPlayerUIEventsObserver> {

    private final List<MediaPlayerUIEventsObserver> observers = new ArrayList<>();
    private final Color background = Color.DARK_GRAY;

    private JLabel playStopButton;
    private JLabel nextButton;
    private JLabel beakButton;
    private JLabel roundButton;
    private JLabel randomButton;

    private JLabel trackLabel;
    private JLabel additionalLabel;
    private JLabel imageLabel;
    private JSlider volumeSlider;
    private JProgressBar progressBar;

    private ImageIcon imageIcon;
    private ImageIcon playStopIcon;
    private ImageIcon roundIcon;
    private ImageIcon randomIcon;

    public MediaPlayerView() {
        initIcons();
        initWindowSettings();

        JPanel titlePanel = createTitlePanel();
        JPanel controlPanel = createControlPanel();
        JPanel imagePanel = createImagePanel();
        JPanel trackPanel = createTrackPanel();
        JPanel volumePanel = createVolumePanel();
        JPanel progressBarPanel = createProgressBarPanel();

        JPanel mainPanel = createMainPanel(titlePanel, controlPanel, imagePanel, trackPanel, volumePanel,
                progressBarPanel);
        add(mainPanel, BorderLayout.CENTER);

        setVolume(50);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                StreamHandler.stopStreams();
                System.out.println("Window is closing. Stopping streams.");
            }
        });

        setupEventHandlers();
        setVisible(true);
    }

    private void initIcons() {
        imageIcon = new ImageIcon();
        playStopIcon = new ImageIcon();
        roundIcon = new ImageIcon();
        randomIcon = new ImageIcon();

        scaleImageIcon(imageIcon, ImageResources.defaultImg, 300, 300);
        scaleImageIcon(playStopIcon, ImageResources.playImg, 50, 50);
        scaleImageIcon(roundIcon, ImageResources.roundImgOff, 25, 25);
        scaleImageIcon(randomIcon, ImageResources.randomImgOff, 25, 25);
    }

    private void initWindowSettings() {
        getContentPane().setBackground(background);
        setTitle("Media Player");
        setSize(500, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
    }

    private void scaleImageIcon(ImageIcon targetIcon, BufferedImage sourceImg, int width, int height) {
        targetIcon.setImage(sourceImg.getScaledInstance(width, height, Image.SCALE_SMOOTH));
    }

    private JPanel createTitlePanel() {
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        titlePanel.setBackground(background);

        JLabel titleLabel = new JLabel("Media Player");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);

        ImageIcon logo = new ImageIcon(ImageResources.logo.getScaledInstance(30, 30, Image.SCALE_SMOOTH));
        JLabel jLogo = new JLabel(logo);

        titlePanel.add(jLogo);
        titlePanel.add(titleLabel);
        titlePanel.setPreferredSize(new Dimension(300, 50));
        return titlePanel;
    }

    private JPanel createImagePanel() {
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setPreferredSize(new Dimension(300, 300));
        imagePanel.setBackground(background);

        imageLabel = new JLabel(imageIcon);
        imagePanel.add(imageLabel, BorderLayout.CENTER);
        return imagePanel;
    }

    private JPanel createTrackPanel() {
        JPanel trackPanel = new JPanel();
        trackPanel.setLayout(new BoxLayout(trackPanel, BoxLayout.Y_AXIS));
        trackPanel.setPreferredSize(new Dimension(300, 50));
        trackPanel.setBackground(background);

        trackLabel = new JLabel("Current Track: None");
        trackLabel.setFont(new Font("Arial", Font.BOLD, 20));
        trackLabel.setForeground(Color.WHITE);
        trackLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        additionalLabel = new JLabel("No data available");
        additionalLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        additionalLabel.setForeground(Color.LIGHT_GRAY);
        additionalLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        trackPanel.add(trackLabel);
        trackPanel.add(additionalLabel);
        return trackPanel;
    }

    private JPanel createVolumePanel() {
        JPanel volumePanel = new JPanel(new BorderLayout());
        volumeSlider = new JSlider(0, 100, 50);
        volumeSlider.setMajorTickSpacing(10);
        volumeSlider.setMinorTickSpacing(5);
        volumeSlider.setBackground(background);
        volumeSlider.setForeground(Color.WHITE);
        volumeSlider.setPreferredSize(new Dimension(200, 50));

        // volumePanel.add(volumeSlider);
        return volumePanel;
    }

    private JPanel createProgressBarPanel() {
        JPanel progressPanel = new JPanel(new BorderLayout());
        progressPanel.setBackground(background);

        progressBar = new JProgressBar(0, 100);
        progressBar.setBackground(background);
        progressBar.setForeground(Color.WHITE);
        progressBar.setPreferredSize(new Dimension(3, 6));

        progressPanel.add(progressBar, BorderLayout.CENTER);
        return progressPanel;
    }

    private JPanel createControlPanel() {
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 35, 0));
        controlPanel.setBackground(background);

        ImageIcon nextIcon = new ImageIcon(ImageResources.next.getScaledInstance(25, 25, Image.SCALE_SMOOTH));
        nextButton = new JLabel(nextIcon);

        ImageIcon backIcon = new ImageIcon(ImageResources.back.getScaledInstance(25, 25, Image.SCALE_SMOOTH));
        beakButton = new JLabel(backIcon);

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

    private JPanel createMainPanel(JPanel titlePanel, JPanel controlPanel, JPanel imagePanel,
            JPanel trackPanel, JPanel volumePanel, JPanel progressBarPanel) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(background);
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

    private void setupEventHandlers() {
        addClickListener(playStopButton, () -> notifyObservers(MediaPlayerUIEventsObserver::onPlayPauseClicked));
        addClickListener(beakButton, () -> notifyObservers(MediaPlayerUIEventsObserver::onBackClicked));
        addClickListener(nextButton, () -> notifyObservers(MediaPlayerUIEventsObserver::onNextClicked));
        addClickListener(roundButton, () -> notifyObservers(MediaPlayerUIEventsObserver::onRepeatClicked));
        addClickListener(randomButton, () -> notifyObservers(MediaPlayerUIEventsObserver::onShuffleClicked));

        volumeSlider.addChangeListener(e -> {
            if (!volumeSlider.getValueIsAdjusting()) {
                int level = volumeSlider.getValue();

                notifyObservers(observer -> observer.onVolumeChanged(level));
            }
        });
    }

    private void addClickListener(JLabel label, Runnable action) {
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                action.run();
            }
        });
    }

    public void setTrackLabel(String text) {
        trackLabel.setText(text);
    }

    public void setAdditionalLabel(String text) {
        additionalLabel.setText(text);
    }

    public void setImage(BufferedImage img) {
        scaleImageIcon(imageIcon, img, 300, 300);
        imageLabel.revalidate();
        imageLabel.repaint();
    }

    public void setPlayStopIcon(BufferedImage img) {
        scaleImageIcon(playStopIcon, img, 50, 50);
        playStopButton.revalidate();
        playStopButton.repaint();
    }

    public void setRoundIcon(BufferedImage img) {
        scaleImageIcon(roundIcon, img, 25, 25);
        roundButton.revalidate();
        roundButton.repaint();
    }

    public void setRandomStopIcon(BufferedImage img) {
        scaleImageIcon(randomIcon, img, 25, 25);
        randomButton.revalidate();
        randomButton.repaint();
    }

    public int getVolume() {
        return volumeSlider.getValue();
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

    @Override
    public void addObserver(MediaPlayerUIEventsObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(MediaPlayerUIEventsObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(Consumer<MediaPlayerUIEventsObserver> action) {
        for (MediaPlayerUIEventsObserver observer : observers) {
            action.accept(observer);
        }
    }
}