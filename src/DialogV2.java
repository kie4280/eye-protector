import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Timer;
import java.util.TimerTask;

public class DialogV2 extends JDialog {

    final long usetime = 10;//change
    final long resttime = 10;//change
    final long postponetime = 5;//change

    private JPanel contentPane;
    private JButton Button;
    private JLabel label1;
    private JSlider slider1;
    private JPasswordField passwordField1;
    private JPanel locker;
    public boolean tried = false;
    Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
    public int sheight = dimension.height;
    public int swidth = dimension.width;
    int mode = 0;
    long lastUsetime = 0;
    long usetimeleft = usetime;//change
    long tempsecond = resttime;
    boolean locked = false;

    String passwd = "0384591";
    Timer counttimer;
    Timer delaytimer;

    public DialogV2() {
        setContentPane(contentPane);
        setModal(false);
        setLocationRelativeTo(null);
        setTitle("You have been watching the computer for too long!");
        setSize(swidth + 10, sheight + 70);
        setLocation(swidth / 2 - getWidth() / 2, sheight / 2 - getHeight() / 2);
        setFocusable(true);
        setAlwaysOnTop(true);
        setResizable(false);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        getRootPane().setDefaultButton(Button);
        passwordField1.setVisible(false);
        passwordField1.setEchoChar('●');
        delaytimer = new Timer("delay");
        counttimer = new Timer("count");


        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.isAltDown() && e.isControlDown() && e.getKeyCode() == KeyEvent.VK_C) {
                    counttimer.cancel();
                    delaytimer.cancel();
                    onclose();
                }
            }

        });

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                //tryclose();
            }
        });
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //tryclose();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        slider1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                int sliderval = slider1.getValue();
                if (!locked && sliderval >= 90) {

                    locked = true;
                    slider1.setValue(100);
                    Button.setVisible(false);
                    Button.setEnabled(false);
                    passwordField1.setVisible(true);

                } else if (locked && sliderval <= 10) {
                    if (String.valueOf(passwordField1.getPassword()).contentEquals(passwd)) {
                        Button.setVisible(true);
                        Button.setEnabled(true);
                        passwordField1.setVisible(false);
                        passwordField1.setText("");
                        slider1.setValue(0);
                        locked = false;
                    } else {
                        passwordField1.setEchoChar((char) 0);
                        passwordField1.setText("Password incorrect!!!");
                        slider1.setValue(100);
                        delaytimer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                passwordField1.setText("");
                                passwordField1.setEchoChar('●');
                            }
                        }, 1000);

                    }
                } else if (sliderval > 10 && sliderval < 90) {
                    if (locked) {
                        slider1.setValue(100);
                    } else {
                        slider1.setValue(0);
                    }
                }
            }
        });

        Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                switch (mode) {
                    case 0:
                        if ((System.currentTimeMillis() - lastUsetime) / 1000 + usetimeleft <= usetime) {//change
                            usetimeleft += (System.currentTimeMillis() - lastUsetime) / 1000;
                        } else {
                            usetimeleft = usetime;//change
                        }
                        label1.setText("You have " + usetimeleft + " seconds. Enjoy!");
                        Button.setVisible(false);
                        locker.setVisible(false);
                        mode = 1;

                        delaytimer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                lastUsetime = System.currentTimeMillis();
                                label1.setText("Stop at any time!");
                                Button.setText("Stop");
                                Button.setVisible(true);
                                setAlwaysOnTop(false);
                                pack();
                                setLocation(swidth - getWidth(), sheight - getHeight() - 50);
                                delaytimer.schedule(new TimerTask() {
                                    @Override
                                    public void run() {
                                        mode = 2;
                                        Button.setVisible(false);
                                        stopuse();
                                    }
                                }, usetimeleft * 1000);
                            }
                        }, 2000);

                        break;
                    case 1:

                        stopDelay();
                        label1.setText("You've spent " + (System.currentTimeMillis() - lastUsetime) / 1000 + " seconds");
                        Button.setText("Use");
                        locker.setVisible(true);
                        pack();
                        setSize(swidth + 10, sheight + 70);
                        setLocation(swidth / 2 - getWidth() / 2, sheight / 2 - getHeight() / 2);
                        setAlwaysOnTop(true);
                        mode = 0;
                        usetimeleft -= (System.currentTimeMillis() - lastUsetime) / 1000;
                        lastUsetime = System.currentTimeMillis();
                        break;

                    case 2:

                        label1.setText("You have five more minutes");
                        locker.setVisible(false);
                        Button.setVisible(false);
                        Button.setEnabled(false);
                        stopCount();
                        delaytimer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                setVisible(false);
                            }
                        }, 2000);
                        delaytimer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                setVisible(true);
                                label1.setText("Time is up!");
                                countdown(false);
                            }
                        }, postponetime * 1000 + 2000);
                        break;
                }

            }
        });

    }

    public void stopuse() {

        label1.setText("You have 10 seconds to end your work");
        pack();
        setLocation(swidth / 2 - getWidth() / 2, sheight / 2 - getHeight() / 2);
        setAlwaysOnTop(true);
        setVisible(true);

        delaytimer.schedule(new TimerTask() {
            @Override
            public void run() {
                setVisible(false);
            }
        }, 2000);

        delaytimer.schedule(new TimerTask() {
            @Override
            public void run() {
                setVisible(true);
                label1.setText("Time is up! Expanding......");
                pack();
                setLocation(swidth / 2 - getWidth() / 2, sheight / 2 - getHeight() / 2);

                int divide = 15;
                int a = (swidth - getWidth()) / divide;
                int b = (sheight - getHeight()) / divide;
                for (int i = 1; i < divide + 1; i++) {
                    delaytimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            setSize(getWidth() + a, getHeight() + b);
                            setLocation(swidth / 2 - getWidth() / 2, sheight / 2 - getHeight() / 2);
                        }
                    }, 1000 * i);
                }

                delaytimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        mode = 2;
                        Button.setText("Postpone");
                        Button.setVisible(true);
                        locker.setVisible(true);
                        countdown(true);
                        setSize(swidth + 10, sheight + 70);
                        setLocation(swidth / 2 - getWidth() / 2, sheight / 2 - getHeight() / 2);
                    }
                }, divide * 1000 + 2000);
            }
        }, 12000);
    }

    public void tryclose() {
// add your code here
        if (!tried) {
            tried = true;
            label1.setText("Want to close me? I don't think so");

            delaytimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    label1.setText("Your waiting time is prolonged!");

                    if (getHeight() <= 500) {
                        pack();
                        setLocation(swidth / 2 - getWidth() / 2, sheight / 2 - getHeight() / 2);
                    }
                }
            }, 1000);

            delaytimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    label1.setText("Reseting the counter!");
                    if (getHeight() <= 500) {
                        pack();
                        setLocation(swidth / 2 - getWidth() / 2, sheight / 2 - getHeight() / 2);
                    }
                }
            }, 2000);

            delaytimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    countdown(true);
                }
            }, 3000);
        }
    }

    public void countdown(boolean restart) {

        if (restart) {
            tempsecond = resttime;
            counttimer.schedule(new TimerTask() {

                @Override
                public void run() {
                    if (tempsecond <= 0) {
                        stopCount();
                        label1.setText("You can use your computer for another 30 minutes!");
                        delaytimer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                label1.setText("Press the button to use");
                                Button.setText("Use");
                                locker.setVisible(true);
                                Button.setEnabled(true);
                                Button.setVisible(true);
                                mode = 0;
                                usetimeleft = usetime;
                            }
                        }, 2000);

                    } else {
                        label1.setText("You have : " + tempsecond + " seconds left");
                        tempsecond--;
                    }
                }
            }, 1000, 1000);
        } else {
            counttimer.schedule(new TimerTask() {

                @Override
                public void run() {

                    if (tempsecond <= 0) {
                        stopCount();
                        label1.setText("You can use your computer for another 30 minutes!");
                        delaytimer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                label1.setText("Press the button to use");
                                Button.setText("Use");
                                locker.setVisible(true);
                                Button.setEnabled(true);
                                Button.setVisible(true);
                                mode = 0;
                                usetimeleft = usetime;
                            }
                        }, 2000);

                    } else {
                        label1.setText("You have : " + tempsecond + " seconds left");
                        tempsecond--;
                    }

                }
            }, 1000, 1000);
        }

    }

    public void onclose() {
        dispose();
    }

    private void stopDelay() {
        if(delaytimer != null) {
            delaytimer.cancel();
        }
        delaytimer = new Timer("delay");
    }

    private void stopCount() {
        if (counttimer != null) {
            counttimer.cancel();
        }
        counttimer = new Timer("count");
    }

}
