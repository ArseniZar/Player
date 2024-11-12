package gui;

import javax.swing.*;
import javax.swing.event.ChangeListener;

import stream.StreamHandler;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

public class MediaPlayerView extends JFrame {
    // Кнопки управления
    private JButton playStopButton;
    private JButton backButton;
    private JButton nextButton;

    private JLabel trackLabel;
    private JLabel imageLabel;
    private JSlider volumeSlider;
    private JProgressBar progressBar;
    private ImageIcon imageIcon;

    public MediaPlayerView(int level, String defaultImg) {
        // Настройки окна
        this.imageIcon = new ImageIcon(defaultImg);
        this.imageIcon.setImage(imageIcon.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH));
        getContentPane().setBackground(Color.DARK_GRAY);
        setTitle("Media Player");
        setSize(500, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel titlePanel = createTitlePanel();
        JPanel controlPanel = createControlPanel();
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

    private JPanel createVolumeSlider() {
        JPanel volumePanel = new JPanel(new BorderLayout());
        volumeSlider = new JSlider(0, 100, 50);
        volumeSlider.setMajorTickSpacing(10);
        volumeSlider.setMinorTickSpacing(5);
        volumeSlider.setBackground(Color.DARK_GRAY);
        volumeSlider.setPreferredSize(new Dimension(200, 50));
        volumePanel.add(volumeSlider);
        return volumePanel;
    }

    private JPanel createProgressBar() {
        JPanel progressPanel = new JPanel(new BorderLayout());
        progressPanel.setBackground(Color.DARK_GRAY);

        progressBar = new JProgressBar(0, 100);
        progressBar.setPreferredSize(new Dimension(150, 6));
        progressBar.setBackground(Color.DARK_GRAY);
        progressBar.setForeground(Color.WHITE);
        progressPanel.add(progressBar, BorderLayout.CENTER);
        return progressPanel;
    }

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

    private Image createImageIconFromBufferedImage(BufferedImage bufferedImage) {
        if (bufferedImage != null) {
            Image image = bufferedImage;
            return image;
        } else {
            System.err.println("BufferedImage is null.");
            return null;
        }
    }

    private JPanel createImagePanel() {
        JPanel imagePanel = new JPanel();
        imagePanel.setPreferredSize(new Dimension(300, 300));
        imagePanel.setBackground(Color.DARK_GRAY);
        imagePanel.setLayout(new BorderLayout());
        // Image image = imageIcon.getImage();
        // imageIcon = new ImageIcon(image);
        imageLabel = new JLabel(imageIcon);
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

    private JPanel createMainPanel(JPanel titlePanel, JPanel controlPanel, JPanel imagePanel, JPanel trackPanel,
            JPanel volumePanel, JPanel progressBarPanel) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.DARK_GRAY);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 25, 10, 25);

        gbc.gridwidth = 2;
        panel.add(titlePanel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        panel.add(imagePanel, gbc);

        gbc.gridy = 2;
        panel.add(trackPanel, gbc);

        gbc.gridy = 3;
        panel.add(volumePanel, gbc);

        gbc.gridy = 4;
        panel.add(progressBarPanel, gbc);

        gbc.gridy = 5;
        panel.add(controlPanel, gbc);

        return panel;
    }

    public void setTrackLabel(String text) {
        trackLabel.setText(text);
    }

    public int getVolume() {
        return volumeSlider.getValue();
    }

    public void setImage(BufferedImage img) {
        imageIcon.setImage(createImageIconFromBufferedImage(img).getScaledInstance(300, 300, Image.SCALE_SMOOTH));
        imageLabel.revalidate();
        imageLabel.repaint();
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

    public void addPlayStopListener(ActionListener listenForPlayStopButton) {
        playStopButton.addActionListener(listenForPlayStopButton);
    }

    public void addBackListener(ActionListener listenForBackButton) {
        backButton.addActionListener(listenForBackButton);
    }

    public void addNextListener(ActionListener listenForNextButton) {
        nextButton.addActionListener(listenForNextButton);
    }

    public void setPlayStopButtonText(String text) {
        playStopButton.setText(text);
    }
}
