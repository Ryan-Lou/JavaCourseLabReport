import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class WelcomeJFrame extends JFrame {
    public WelcomeJFrame(String[] texts) {
        super("滚动字");
        this.setBounds(300, 300, 740, 300);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        if (texts == null || texts.length == 0) {
            this.getContentPane().add(new RollbyJPanel("Welcome!"));
        } else {
            this.getContentPane().setLayout(new GridLayout(texts.length, 1));

            for (String text : texts) {
                this.getContentPane().add(new RollbyJPanel(text));
            }
        }
        this.setVisible(true);
    }

    public WelcomeJFrame() {
        this(null);
    }

    private class RollbyJPanel extends JPanel implements ActionListener, Runnable {
        JTextField text_word, texts[];
        JButton buttons[];
        Thread thread;
        int sleeptime;
        Font font = new Font("宋体", Font.PLAIN, 20);

        public RollbyJPanel(String text) {
            this.setLayout(new GridLayout(2, 1));
            this.text_word = new JTextField(String.format("%65s", text));
            this.add(this.text_word);
            this.text_word.setFont(font);

            JPanel cmdpanel = new JPanel();
            this.add(cmdpanel);

            String[] textstr = {"sleeptime", "State1", "State2", "isAlive"};
            this.texts = new JTextField[textstr.length];
            for (int i = 0; i < this.texts.length; i++) {
                cmdpanel.add(new JLabel(textstr[i]));
                cmdpanel.add(this.texts[i] = new JTextField(8));
                this.texts[i].setEditable(false);
            }

            this.sleeptime = (int) (Math.random() * 100);
            this.texts[0].setText("" + this.sleeptime);
            this.texts[0].setEditable(true);
            this.texts[0].addActionListener(this);

            String[] buttonstr = {"启动", "中断"};
            this.buttons = new JButton[buttonstr.length];
            for (int i = 0; i < this.buttons.length; i++) {
                cmdpanel.add(this.buttons[i] = new JButton(buttonstr[i]));
                this.buttons[i].addActionListener(this);
            }

            this.buttons[1].setEnabled(false);

            this.thread = new Thread(this, text);
        }

        public void run() {
            while (true) {
                try {
                    String str = this.text_word.getText();
                    this.text_word.setText(str.substring(1) + str.substring(0, 1));
                    Thread.sleep(this.sleeptime);
                } catch (InterruptedException ex) {
                    break;
                }
            }
        }

        public void actionPerformed(ActionEvent event) {
            if (event.getSource() == this.texts[0]) {
                try {
                    this.sleeptime = Integer.parseInt(this.texts[0].getText());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "\"" + this.texts[0].getText() + "\" 不能转换为整数，请重新输入！");
                }
            } else if (event.getSource() == this.buttons[0]) {
                if (this.thread.getState() != Thread.State.NEW) {
                    this.thread = new Thread(this, this.text_word.getText().trim());
                }
                this.thread.start();
                this.texts[1].setText("" + this.thread.getState());
                this.buttons[0].setEnabled(false);
                this.buttons[1].setEnabled(true);
                this.texts[2].setText("" + this.thread.getState());
            } else if (event.getSource() == this.buttons[1]) {
                this.thread.interrupt();
                this.texts[1].setText("" + this.thread.getState());
                this.buttons[0].setEnabled(true);
                this.buttons[1].setEnabled(false);
                this.texts[2].setText("" + this.thread.getState());
            }
            this.texts[3].setText("" + this.thread.isAlive());
        }
    }

    public static void main(String[] arg) {
        String texts[] = {"Welcome", "Hello", "Rollby"};
        new WelcomeJFrame(texts);
    }
}
