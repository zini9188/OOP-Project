package app.frame;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;

public class GameTitleFrame extends JFrame {
    private Image gameTitle = new ImageIcon("image/GameTitle.png").getImage();
    private ImageIcon sound = new ImageIcon("image/sound.png");
    private ImageIcon mute = new ImageIcon("image/mute.png");
    private Font font = new Font("궁서", Font.BOLD, 30);
    private Clip clip;
    private boolean isMute = false;

    public GameTitleFrame() {
        super();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(new GameTitlePanel());
        setBounds(1200, 500, 600, 600);

        // bgm을 무한 루프한다.
        loadAudio();
        clip.loop(Clip.LOOP_CONTINUOUSLY);
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(-30.0f);
        clip.start();

        setVisible(true);
    }

    // bgm을 받아와 오픈한다.
    private void loadAudio() {
        try {
            clip = AudioSystem.getClip();
            File audioFile = new File("audio/JourneyintheNewWorld.wav");
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            clip.open(audioStream);
        } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }
    }


    class GameTitlePanel extends JPanel {
        public GameTitlePanel() {
            setLayout(null);
            JLabel gameTitle = new JLabel("떨어지는 미사일 막기");
            gameTitle.setBounds(85, 50, 600, 50);
            gameTitle.setForeground(Color.WHITE);
            gameTitle.setFont(new Font("궁서",
                    Font.BOLD, 40));

            JLabel gameStartBtn = new JLabel("게임 시작");
            gameStartBtn.setBounds(210, 100, 170,
                    150);
            gameStartBtn.setForeground
                    (Color.BLACK);
            gameStartBtn.setFont(font);

            // label에 마우스가 들어오고 나가면 색상을 변경
            gameStartBtn.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    gameStartBtn.setForeground(Color.lightGray);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    gameStartBtn.setForeground(Color.BLACK);

                }

                @Override
                public void mousePressed(MouseEvent e) {
                    new GameFrame();
                    clip.stop();
                    dispose();
                }
            });

            JLabel infoBtn = new JLabel("게임 정보");
            infoBtn.setBounds(210, 240, 170, 150);
            infoBtn.setForeground(Color.BLACK);
            infoBtn.setFont(font);
            // label에 마우스가 들어오고 나가면 색상을 변경
            infoBtn.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    infoBtn.setForeground(Color.lightGray);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    infoBtn.setForeground(Color.BLACK);
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    JOptionPane.showMessageDialog(null, "1771365 박호제", "제작", JOptionPane.INFORMATION_MESSAGE);
                }
            });

            JLabel exitBtn = new JLabel("끝내기");
            exitBtn.setBounds(230, 380, 170, 150);
            exitBtn.setForeground(Color.BLACK);
            exitBtn.setFont(font);
            // label에 마우스가 들어오고 나가면 색상을 변경
            exitBtn.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    exitBtn.setForeground(Color.lightGray);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    exitBtn.setForeground(Color.BLACK);
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    System.exit(0);
                }
            });

            JLabel soundBtn = new JLabel(sound);
            soundBtn.setBounds(500, 500, 60, 60);
            soundBtn.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    if (!isMute) {
                        // isMute가 false이면 음소거 후 true로 변경
                        isMute = !isMute;
                        soundBtn.setIcon(mute);
                        clip.setFramePosition(0);
                        clip.stop();

                    } else {
                        // isMute가 true이면 음소거를 해제하고 false로 변경
                        isMute = !isMute;
                        soundBtn.setIcon(sound);
                        clip.start();
                    }
                }
            });
            add(gameTitle);
            add(gameStartBtn);
            add(infoBtn);
            add(exitBtn);
            add(soundBtn);
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(gameTitle, 0, 0, this);
        }
    }
}