package view;

import client.CacheUnitClientObserver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Scanner;

public class CacheUnitClientView
        extends java.lang.Object {

    public final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    public FrameObjects buttons;
    int requestCounter = 0;

    public CacheUnitClientView() {
        new CacheUnitClientObserver();

    }

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        this.pcs.addPropertyChangeListener(pcl);

    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        this.pcs.removePropertyChangeListener(pcl);

    }

    public void start() {
        startGUI();
    }

    public void startGUI()  {
        JFrame frame = new JFrame("MMU");

        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        buttons = new FrameObjects();
        buttons.setOpaque(true);
        frame.setContentPane(buttons);

        frame.getContentPane().setLayout(null);

        frame.setVisible(true);

    }

    @SuppressWarnings("resource")
    public <T> void updateUIData(T t) {

        String input = t.toString();
        StringBuilder builder = new StringBuilder();

        Scanner scanner = new Scanner(input);
        scanner.useDelimiter(",");
        builder.append("Chosen Cache algorithm: " + scanner.next() + "\n");
        builder.append("Cache capacity: " + scanner.nextInt() + "\n");
        requestCounter += scanner.nextInt();
        builder.append("Total Num of requests: " + requestCounter + "\n");
        builder.append("Total Num of datamodels: " + scanner.nextInt() + "\n");
        builder.append("Total Num of swaps: " + scanner.nextInt() + "\n");

        this.buttons.statArea.setText(builder.toString());
    }

    @SuppressWarnings("serial")
    public class FrameObjects extends JPanel implements ActionListener {

        JButton loadButton, statButton, cacheAlgoButton;
        JTextArea statArea;
        JRadioButton LruButton, NruButton, RandomButton;
        JTextField capacityText;
        JLabel capacityLabel;
        String algo = null;
        String cacheAlgo = null;

        public FrameObjects() {

            loadButton = new JButton("Load a Request");
            statButton = new JButton("Show Statistics");
            cacheAlgoButton = new JButton("Replace Algorithm");
            statArea = new JTextArea();
            LruButton = new JRadioButton();
            NruButton = new JRadioButton();
            RandomButton = new JRadioButton();
            ButtonGroup group = new ButtonGroup();
            capacityText = new JTextField(2);
            capacityLabel = new JLabel("Capacity:");

            Image loadImg = new ImageIcon(this.getClass().getResource("/resources/load.jpg")).getImage();
            Image statImg = new ImageIcon(this.getClass().getResource("/resources/stat.png")).getImage();
            Image cacheImg = new ImageIcon(this.getClass().getResource("/resources/cache.png")).getImage();

            setButton(loadButton, new ImageIcon(loadImg), 20, 5, 170, 50);
            setButton(statButton, new ImageIcon(statImg), loadButton.getWidth() + loadButton.getX(), 5, 170, 50);
            setButton(cacheAlgoButton, new ImageIcon(cacheImg), 20, 250, 200, 50);
            setRadioButton(LruButton, "LRU", group, 230, 245, 70, 20);
            setRadioButton(NruButton, "NRU", group, 230, 265, 70, 20);
            setRadioButton(RandomButton, "RANDOM", group, 230, 285, 85, 20);
            capacityText.setBounds(315, 260, 40, 20);
            capacityLabel.setBounds(310, 240, 90, 25);

            setArea(statArea);
            statArea.setBackground(this.getBackground());

            add(loadButton);
            add(statButton);
            add(cacheAlgoButton);
            add(LruButton);
            add(NruButton);
            add(RandomButton);
            add(statArea);
            add(capacityText);
            add(capacityLabel);

        }

        private void setRadioButton(JRadioButton button, String name, ButtonGroup group, int xPivot, int yPivot, int width, int height) {

            button.setText(name);
            button.setBounds(xPivot, yPivot, width, height);
            button.setActionCommand(name);
            group.add(button);
            button.addActionListener(this);
        }

        @Override
        public void actionPerformed(ActionEvent e) {

            if (e.getSource() == LruButton || e.getSource() == NruButton || e.getSource() == RandomButton) {

                algo = e.getActionCommand();
            }

            if (e.getSource() == loadButton || e.getSource() == statButton || e.getSource() == cacheAlgoButton) {

                if (e.getSource() == loadButton) {

                    JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir")+"\\src\\resources");

                    if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
                        pcs.firePropertyChange("FilePath", null, fileChooser.getSelectedFile().getAbsolutePath());
                }

                if (e.getSource() == statButton) {

                    pcs.firePropertyChange("Stats", null, null);
                }

                if (e.getSource() == cacheAlgoButton) {

                    int capa = 0;

                    try {
                        capa = Integer.parseInt(capacityText.getText());

                        if (capa <= 0) {
                            JOptionPane.showMessageDialog(null, "Wrong capacity input, setting default - 4");
                            capa = 4;
                        }

                    }
                    catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Wrong capacity input, setting default - 4");
                        capa = 4;
                    }
                    finally {
                        if (algo == null)	{
                            JOptionPane.showMessageDialog(null, "No cache algorithm was selected, setting default - RANDOM");
                            algo = "RANDOM";
                        }

                        cacheAlgo = algo + "," + String.valueOf(capa);
                        pcs.firePropertyChange("SwitchCache", null, cacheAlgo);
                    }




                }
            }
        }

        public void setButton(JButton button, ImageIcon icon, int xPivot, int yPivot, int width, int height) {
            button.setBounds(xPivot, yPivot, width, height);
            button.setIcon(icon);
            button.addActionListener(this);

            if (button.getText().equals("Load a Request"))
                button.setActionCommand("load");
            else if (button.getText().equals("Show Statistics"))
                button.setActionCommand("stat");
            else
                button.setActionCommand("cache");
        }
        public void setArea(JTextArea statArea) {
            statArea.setBounds(20, 90, 400, 100);
            statArea.setEditable(false);
        }

    }	// end of WindowButton Class

}	// end of CacheUnitView Class
