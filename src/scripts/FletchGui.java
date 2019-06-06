package scripts;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class FletchGui {
    private JComboBox comboBox1;
    private JButton startButton;
    private JPanel main;

    public FletchGui() {
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        comboBox1.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                System.out.println("Event = "+e.getItem());
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Fletcher");
        frame.setContentPane(new FletchGui().main);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
//        frame.setSize(450, 450);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }
    public void start(){

    }
}
