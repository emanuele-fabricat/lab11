package it.unibo.oop.reactivegui02;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * Second example of reactive GUI.
 */
@SuppressWarnings("PMD.AvoidPrintStackTrace")
public final class ConcurrentGUI extends JFrame {
    private static final double WIDTH_PERC = 0.2;
    private static final double HEIGHT_PERC = 0.1;
    private static final int COUNT_STOP = 0;
    private static final int COUNT_UP = 1;
    private static final int COUNT_DOWN = 2;
    
    private final JPanel canva = new JPanel();
    private final JLabel counter = new JLabel("0");
    private final JButton up = new JButton("up");
    private final JButton down = new JButton("down");
    private final JButton stop = new JButton("stop");
    private final Agent agent = new Agent();

    /**
     * constructor of the GUI
     */
    public ConcurrentGUI() {
        final Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize((int) (screen.getWidth() * WIDTH_PERC), (int) (screen.getHeight() * HEIGHT_PERC));
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.add(this.canva);
        this.canva.add(this.counter);
        this.canva.add(this.up);
        this.canva.add(this.down);
        this.canva.add(this.stop);
        this.setVisible(true);
        this.up.addActionListener((e) -> agent.changeMode(COUNT_UP));
        this.down.addActionListener((e) -> agent.changeMode(COUNT_DOWN));
        this.stop.addActionListener((e) -> agent.changeMode(COUNT_STOP));
        new Thread(agent).start();
    }

    private class Agent implements Runnable {
        private volatile int mode = COUNT_UP;
        private int counter;


        @Override
        public void run() {
            while (mode != COUNT_STOP) {
                try {
                    // The EDT doesn't access `counter` anymore, it doesn't need to be volatile 
                    final var nextText = Integer.toString(this.counter);
                    SwingUtilities.invokeAndWait(() -> ConcurrentGUI.this.counter.setText(nextText));
                    switch(mode) {
                        case COUNT_UP -> this.counter++;
                        case COUNT_DOWN -> this.counter--;
                    }
                    Thread.sleep(100);
                } catch (InvocationTargetException | InterruptedException ex) {
                    /*
                        * This is just a stack trace print, in a real program there
                        * should be some logging and decent error reporting
                        */
                    ex.printStackTrace();
                }

            }
        }

        public void changeMode(final int mode) {
            this.mode = mode;
        }
    }
}
