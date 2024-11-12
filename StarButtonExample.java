import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StarButtonExample extends JButton {
    public StarButtonExample(String text) {
        super(text);
        setContentAreaFilled(false); // Убираем стандартный фон
        setFocusPainted(false); // Убираем подсветку
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.YELLOW);
        Polygon star = new Polygon();
        
        // Нарисуем звезду
        star.addPoint(30, 0);
        star.addPoint(40, 20);
        star.addPoint(60, 20);
        star.addPoint(45, 35);
        star.addPoint(50, 55);
        star.addPoint(30, 45);
        star.addPoint(10, 55);
        star.addPoint(15, 35);
        star.addPoint(0, 20);
        star.addPoint(20, 20);
        
        g2d.fillPolygon(star);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Star Button Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.setLayout(new FlowLayout());

        StarButtonExample starButton = new StarButtonExample("Click Me");
        starButton.setPreferredSize(new Dimension(100, 100)); // Размер кнопки

        starButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Star Button clicked!");
            }
        });

        frame.add(starButton);
        frame.setVisible(true);
    }
}
