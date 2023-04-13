package app.panel;

import app.frame.RankFrame;
import app.utils.Words;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class GamePanel extends JPanel {
    private final int maxCountOfWord = 100; // 떨어질 단어 레이블의 개수
    private int curCountOfWord; // 현재 떨어지고 있는 단어 레이블의 개수
    private int time = 30;
    private int rounds = 0;
    private int userLife;
    private int speed;
    private boolean gameOver = false;
    public boolean gameRestart = false;
    private String s = "▒▒▒▒▒▒▒▒▒";

    private final JTextField input = new JTextField(40); // 정답을 입력하는 텍스트 필드
    private final JLabel[] fallingWord = new JLabel[maxCountOfWord]; // 떨어지는 단어 레이블 배열
    private final JLabel[] life = new JLabel[5];
    private JLabel timer;
    private JLabel lifeText;
    private JLabel round;

    private final Font wordFont = new Font("Arial", Font.BOLD, 20);
    private final Font infoFont = new Font("궁서", Font.BOLD, 15);

    private final ImageIcon lifeIcon = new ImageIcon("image/life.png");
    private final ImageIcon loseLifeIcon = new ImageIcon("image/life2.png");
    private final ImageIcon backGround = new ImageIcon("image/backGround.jpg");
    private final ImageIcon backGround2 = new ImageIcon("image/backGround2.jpg");
    private final ImageIcon missileRed = new ImageIcon("image/missileRed.png");
    private final ImageIcon missileBlack = new ImageIcon("image/missileBlack.png");
    private final ImageIcon missileMagenta = new ImageIcon("image/missileMagenta.png");
    private final ImageIcon missileGreen = new ImageIcon("image/missileGreen.png");
    private final ImageIcon explosion = new ImageIcon("image/explosion.png");
    private final Image groundImage = backGround.getImage();
    private final Image groundImage2 = backGround2.getImage();
    private ScorePanel scorePanel = null;
    private final Words textSource = new Words("txt/words.txt"); // 단어 벡터 생성
    private Clip clip;

    public GamePanel(ScorePanel scorePanel) {
        this.scorePanel = scorePanel;
        setLayout(new BorderLayout());
        add(new GameInfoPanel(), BorderLayout.NORTH);
        add(new GameGroundPanel(), BorderLayout.CENTER);
        add(new InputPanel(), BorderLayout.SOUTH);
        // bgm을 무한 반복한다.
        loadAudio();
        clip.loop(Clip.LOOP_CONTINUOUSLY);
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(-30.0f);
        clip.start();

        input.addActionListener(e -> {
            JTextField t = (JTextField) (e.getSource());
            String inputWord = t.getText(); // 텍스트 필드에 입력된 단어를 가져온다.
            for (int i = 0; i < curCountOfWord; i++) { // 떨어지는 단어의 개수만큼
                if (fallingWord[i].getText
                        ().equals(inputWord)) { // 떨어지는 단어 중 입력된 단어와 같은 값이 있는지 판독
                    fallingWord[i].setText
                            (""); // 맞추면 Text를 공백으로 변경
                    fallingWord[i].setIcon
                            (explosion); // 맞추면 터트리는 효과를 부여
                    itemByColorMissile(fallingWord[i]); // 미사일 색깔별 기능을 실행한다.
                }
                t.setText("");
            }
        });
    }

    // bgm을 재시작한다.
    public void restartAudio() {
        clip.setFramePosition(0);
        clip.stop();
    }

    // bgm을 음소거한다.
    public void muteAudio() {
        clip.stop();
    }

    // bgm을 시작한다.
    public void startAudio() {
        clip.start();
    }

    // bgm파일을 받아와 오픈한다.
    private void loadAudio() {
        try {
            clip = AudioSystem.getClip();
            File audioFile = new File("audio/PleaseWait.wav");
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            clip.open(audioStream);
        } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }
    }

    // 색깔별 미사일 기능을 제공해준다.
    public void itemByColorMissile(JLabel word) {
        Color color = word.getForeground(); // 단어의 색상을 파악
        scorePanel.setTextColorLabel("");
        if (color == Color.BLACK) { // 검정색 기능
            // 기본 미사일 기능이 없음
            scorePanel.increase();
        }
        if (color == Color.RED) { // 빨간색 기능
            RedMissItem rItem = new RedMissItem(); // 10초간 점수가 두배가 되는 기능
            scorePanel.setTextColorLabel("10초간 점수 두배");
            rItem.start();
            scorePanel.increase();
        }
        if (color == Color.MAGENTA) { // 보라색 기능
            MagentaMissItem mItem = new MagentaMissItem(); // 속도를 4분의 1로 줄이고 서서히 원래 스피드로 증가
            mItem.start();
            scorePanel.setTextColorLabel("속도 0.25배");
            scorePanel.increase();
        }
        if (color == Color.GREEN) {
            // 사용자의 생명력이 1~4 사이이면 생명력을 회복한다.
            if (userLife < life.length && userLife > 0) {
                userLife++;
                life[userLife - 1].setIcon(lifeIcon);
                scorePanel.setTextColorLabel("라이프 회복");
            }
            scorePanel.increase();
        }
    }

    public void startGame() {
        // 게임 시작시 정보들을 초기화
        curCountOfWord = 0;
        userLife = 5;
        gameOver = false;
        gameRestart = false;
        speed = scorePanel.getSpeed();
        input.setFocusable(true);
        input.requestFocus();
        WordThread thread = new WordThread(this, fallingWord, timer, scorePanel.getPeriod());
        thread.start();
    }

    // 단어 생성 스레드
    class WordThread extends Thread {
        private final GamePanel panel;
        private final JLabel[] word;
        private final JLabel timer;
        private final WordMoveThread[] thread = new
                WordMoveThread[maxCountOfWord];
        private int period;
        private int index;

        public WordThread(GamePanel panel, JLabel[] word, JLabel timer, int period) {
            this.panel = panel;
            this.word = word;
            this.timer = timer;
            this.period = period;
        }

        // period 초에 한번씩 새로운 단어가 내려가도록 한다.
        @Override
        public void run() {
            try {
                index = 0;
                // 타이머 스레드 시작
                GameTimerThread timerThread = new GameTimerThread(timer);
                timerThread.start();
                rounds++;
                round.setText("라운드 " + rounds);
                // 단어를 생성하기 시작한다.
                for (int i = 0; i < maxCountOfWord; i++) {
                    // 유저의 게임 오버 여부를 파악 후 gameOver시 스레드 인터럽트
                    if (userLife <= 0 || rounds > 5) {
                        timerThread.interrupt();
                        gameOver = true;
                        for (int j = 0; j < index; j++) {
                            thread[j].interrupt();
                        }
                        break;
                    }
                    curCountOfWord++; // 현재 생성된 단어의 개수
                    word[i].setText(textSource.getRandomWord());
                    // word가 단어의 길이를 뺀 랜덤한 패널 위치에서 생성된다.
                    int width = word[i].getText().length() * 18 + missileRed.getIconWidth();
                    word[i].setBounds((int) (Math.random() * (panel.getWidth() - width - 30) + 30), 1, width, 40);
                    // 랜덤한 색상의 단어를 만들어낸다.
                    int random = (int) (Math.random() * 16);
                    if (random == 0 || random == 1) {
                        word[i].setForeground(Color.RED);
                        word[i].setIcon(missileRed);
                    } else if (random == 2) {
                        word[i].setForeground(Color.MAGENTA);
                        word[i].setIcon(missileMagenta);
                    } else if (random == 3) {
                        word[i].setForeground(Color.GREEN);
                        word[i].setIcon(missileGreen);
                    } else {
                        word[i].setForeground(Color.BLACK);
                        word[i].setIcon(missileBlack);
                    }

                    // 단어별로 움직일 수 있는 스레드를 생성
                    thread[i] = new WordMoveThread(panel, word[i]);
                    thread[i].start();

                    Thread.sleep(period);
                    index++;
                }
            } catch (InterruptedException e) {
                return;
            }
            if (gameOver) {
                panel.removeAll();
                // 게임이 끝날시 점수를 입력하고 이름을 입력받는다.
                String name = JOptionPane.showInputDialog(null,
                        scorePanel.getScore() + "점을 기록했습니다. 이름을 입력하세요");
                if (name != null) {
                    try {
                        FileWriter fw = new
                                FileWriter("txt/score.txt", true);
                        BufferedWriter bw = new
                                BufferedWriter(fw);
                        bw.write(name);
                        bw.newLine();
                        bw.write(Integer.toString(scorePanel.getScore()));
                        bw.newLine();
                        bw.flush();
                        bw.close();
                    } catch (IOException e) {
                        return;
                    }
                    new RankFrame();
                }
            }
        }
    }

    // 미사일이 움직이게하는 스레드
    class WordMoveThread extends Thread {
        private GamePanel panel;
        private JLabel word;

        public WordMoveThread(GamePanel panel, JLabel word) {
            this.word = word;
            this.panel = panel;
        }

        // 미사일이 땅에 닿을 때까지
        @Override
        public void run() {
            int cx, cy;
            while (userLife > 0 && !gameRestart && word != null) {
                try {
                    // 단어가 화면을 넘어가거나 게임 오버 상태인 경우
                    if (word.getY() > panel.getHeight() - 150 || gameOver) {
                        word.setText("");
                        word.setIcon(explosion);
                        Thread.sleep(200);
                        userLife--;
                        life[userLife].setIcon(loseLifeIcon);
                        interrupt();
                    }
                    // 단어가 공백인경우 인터럽트 시킨다.
                    if (word.getText().equals("")) {
                        interrupt();
                    }
                    // 단어의 현재 좌표를 가져오고 speed만큼 y좌표를 증가시킨다.
                    cx = word.getX();
                    cy = word.getY();
                    cy += speed;
                    word.setLocation(cx, cy);
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    // 인터럽트시 현재 단어를 삭제시키고 return
                    Container c = word.getParent();
                    c.remove(word);
                    c.repaint();
                    return;
                }
            }
            gameOver = true;
            if (gameOver || gameRestart) { // 게임 오버시 추가로 남은 스레드들 종료
                interrupt();
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    Container c = word.getParent();
                    c.remove(word);
                    c.repaint();
                    return;
                }
            }
            repaint();
        }
    }

    // 라운드 시간을 표시하는 스레드
    class GameTimerThread extends Thread {
        private JLabel timer;

        public GameTimerThread(JLabel timer) {
            this.timer = timer;
        }

        @Override
        public void run() {
            time = 30;
            while (true) {
                try {
                    // scorePanel의 slider를 speed에 맞춰 설정해준다.
                    scorePanel.setSlider(speed);
                    timer.setText(s + "남은 시간 " + time);
                    Thread.sleep(1000);
                    if (time <= 0) {
                        // 시간이 0초가 되면 30초로 바꾸고 라운드를 증가하며 speed를 1 증가시킨다.
                        time = 30;
                        rounds++;
                        round.setText("라운드 " + rounds);
                        speed += 1;
                    }
                    time--;
                } catch (InterruptedException e) {
                    return;
                }
            }
        }
    }

    // 10초간 점수 두배를 하는 스레드 빨간색 기능
    class RedMissItem extends Thread {
        @Override
        public void run() {
            int timeLimit = 10;
            scorePanel.setIncScore(200);
            while (true) {
                try {
                    Thread.sleep(1000);
                    timeLimit--;
                    if (timeLimit <= 0) {
                        scorePanel.setIncScore(100);
                        scorePanel.setTextColorLabel("");
                        break;
                    }
                } catch (InterruptedException e) {
                    return;
                }
            }
        }
    }

    // 미사일들의 속도를 4분의 1로 줄였다가 서서히 빨라지는 보라색 기능
    class MagentaMissItem extends Thread {
        @Override
        public void run() {
            int timeLimit = speed;
            speed /= 4;
            while (true) {
                try {
                    Thread.sleep(1200);
                    if (speed < timeLimit) {
                        speed++;
                    } else {
                        scorePanel.setTextColorLabel("");
                        interrupt();
                    }
                } catch (InterruptedException e) {
                    return;
                }
            }
        }
    }

    // 게임 패널의 윗부분
    class GameInfoPanel extends JPanel {
        public GameInfoPanel() {
            setLayout(new FlowLayout(FlowLayout.RIGHT));
            setBackground(Color.BLACK);
            round = new JLabel("라운드 0");
            round.setForeground(Color.WHITE);
            round.setFont(infoFont);
            add(round);

            timer = new JLabel(s + "남은 시간 30");
            timer.setForeground(Color.WHITE);
            timer.setFont(infoFont);
            add(timer);

            lifeText = new JLabel(s + "남은 목숨");
            lifeText.setForeground(Color.WHITE);
            lifeText.setFont(infoFont);
            add(lifeText);

            for (int i = 0; i < life.length; i++) {
                life[i] = new JLabel(lifeIcon);
                add(life[i]);
            }
        }
    }

    // 게임 패널의 중앙부분
    class GameGroundPanel extends JPanel {
        public GameGroundPanel() {
            setLayout(null);
            for (int i = 0; i < maxCountOfWord; i++) {
                fallingWord[i] = new JLabel();
                fallingWord[i].setFont(wordFont);
                add(fallingWord[i]);
            }
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (!gameOver)
                g.drawImage(groundImage, 0, 0, this);

            if (gameOver) {
                g.drawImage(groundImage2, 0, 0, this);
                g.setColor(Color.WHITE);
                g.setFont(new Font("궁서", Font.ITALIC, 60));
                g.drawString("Game Over !!!!", getWidth() / 4, getHeight() / 2);
            }
        }
    }

    // 게임 패널의 하단 부분
    class InputPanel extends JPanel {
        public InputPanel() {
            setLayout(new FlowLayout());
            this.setBackground(Color.darkGray);
            add(input);
        }
    }
}