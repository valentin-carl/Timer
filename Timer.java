import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Timer {

    GUI gui;

    public Timer () {
        this.gui = new GUI();
    }

    public static void main (String[] args) {

        // create new timer instance (is started automatically)
        Timer timer = new Timer();
    }
}

class GUI {

    // gui stuff
    JFrame frame;
    JPanel panel;
    JLabel timerLabel;
    JTextField inputText;
    JButton start;
    JButton stop;

    // timer info
    static int minutesTotal;
    static int msremaining;
    static boolean paused = true;

    // threads stuff
    ArrayList<TimerThread> threads = new ArrayList<>();

    GUI () {
        // create frame
        this.frame = new JFrame("Timer");
        this.frame.setSize(200, 250);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // create panel & add everything to frame/panel
        this.panel = new JPanel();
        frame.add(this.panel);
        this.placeComponents();

        // show frame
        this.frame.setVisible(true);
    }

    private void placeComponents () {
        // set layout (required)
        panel.setLayout(new GridLayout(0, 1));

        // creating the timer label
        this.timerLabel = new JLabel("time");
        this.timerLabel.setHorizontalAlignment(JLabel.CENTER);
        this.timerLabel.setVerticalAlignment(JLabel.CENTER);
        this.timerLabel.setFont(new Font("Helvetica", 0, 28));
        this.timerLabel.setText("Hello, world!");
        this.panel.add(this.timerLabel);

        // creating input field
        this.inputText = new JTextField();
        this.inputText.setBounds(100, 100, 150, 25);
        this.inputText.setHorizontalAlignment(JTextField.CENTER);
        this.panel.add(this.inputText);

        // start button
        this.start = new JButton("start");
        this.panel.add(this.start);

        this.start.addActionListener(new StartButtonListener(this));

        // stop button
        this.stop = new JButton("stop");
        this.panel.add(this.stop);

        this.stop.addActionListener(new StopButtonListener(this));
    }

    void startTimer () {
        TimerThread timer = new TimerThread(this);
        this.threads.add(timer);
        timer.start();
    }
}

class TimerThread extends Thread {

    GUI parent;

    TimerThread (GUI parent) {
        this.parent = parent;
    }

    @Override
    public void run () {
        System.out.println("thread started");
        while (GUI.msremaining > 0 && !GUI.paused) {

            int minsRemaining = (int) GUI.msremaining/60_000;
            int secsRemaining = (GUI.msremaining%60_000)/1000;
            this.parent.timerLabel.setText(minsRemaining + ":" + secsRemaining);
            try {
                Thread.sleep(1000);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            GUI.msremaining -= 1000;
        }
    }
}

class StartButtonListener implements ActionListener {

    GUI parent;

    StartButtonListener (GUI parent) {
        this.parent = parent;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (!GUI.paused && this.parent.inputText.getText().equals("")) {
            // A: läuft und kein neuer input => nichts soll passieren
            System.out.println("no action to perform, A");

        } else if (!GUI.paused && !this.parent.inputText.getText().equals("")) {
            // B: läuft und neuer input => nichts passieren
            System.out.println("no action to perform, B");

        } else if (GUI.paused && this.parent.inputText.getText().equals("")) {
            // C: läuft nicht und kein neuer input => weiterlaufen mit msRemaining
            GUI.paused = false;
            System.out.println("continuing timer, C");
            this.parent.startTimer();

        } else if (GUI.paused && !this.parent.inputText.getText().equals("")) {
            // D: läuft nicht und neuer input => zeit neu setzen und starten
            GUI.paused = false;
            System.out.println("resetting timer, D");

            // get input text & calculate remaining msecs
            GUI.minutesTotal = Integer.parseInt(this.parent.inputText.getText());
            GUI.msremaining = GUI.minutesTotal*60*1000;

            this.parent.startTimer();
        }

        // clear input text window
        this.parent.inputText.setText("");

        // set pause flag to false
        GUI.paused = false;
    }
}

class StopButtonListener implements ActionListener {

    GUI parent;

    StopButtonListener (GUI parent) {
        this.parent = parent;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        // set pause flag to true
        GUI.paused = true;
    }
}