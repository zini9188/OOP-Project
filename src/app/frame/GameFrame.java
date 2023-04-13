package app.frame;

import app.panel.EditPanel;
import app.panel.GamePanel;
import app.panel.ScorePanel;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameFrame extends JFrame {
    // 게임 시작 아이콘
    private final ImageIcon normalIcon = new ImageIcon("image/normal.gif");
    private final ImageIcon pressedIcon = new ImageIcon("image/pressed.gif");
    private final ImageIcon overIcon = new ImageIcon("image/over.gif");

    // 재시작 아이콘
    private final ImageIcon reset0 = new ImageIcon("image/reset0.png");
    private final ImageIcon reset1 = new ImageIcon("image/reset1.png");

    // 랭킹 아이콘
    private final ImageIcon trophy = new ImageIcon("image/trophy.png");

    // 소리 아이콘
    private final ImageIcon sound = new ImageIcon("image/sound.png");
    private final ImageIcon sound2 = new ImageIcon("image/sound2.png");
    private final ImageIcon mute = new ImageIcon("image/mute.png");
    private final ImageIcon mute2 = new ImageIcon("image/mute2.png");

    // 메뉴에 들어갈 아이템들
    private final JMenuItem startItem = new JMenuItem("게임 시작");
    private final JMenuItem mainItem = new JMenuItem("메인 메뉴");
    private final JMenuItem rankItem = new JMenuItem("랭킹");
    private final JMenuItem easyItem = new JMenuItem("쉬움");
    private final JMenuItem normalItem = new JMenuItem("보통");
    private final JMenuItem hardItem = new JMenuItem("어려움");

    // 버튼들에 맞는 아이콘으로 만들어준다.
    private final JButton startBtn = new JButton(normalIcon);
    private final JButton resetBtn = new JButton(reset0);
    private final JButton rankBtn = new JButton(trophy);
    private final JButton soundBtn = new JButton(sound);

    private final ScorePanel scorePanel = new ScorePanel();
    private final EditPanel editPanel = new EditPanel();
    private final GamePanel gamePanel = new GamePanel(scorePanel);

    // 음소거인지 확인하는 변수
    private boolean isMute = false;

    public GameFrame() {
        setTitle("타이핑 게임");
        setDefaultCloseOperation
                (JFrame.EXIT_ON_CLOSE);
        setBounds(1200, 500, 950, 800);
        splitPane(); // JSplitPane을 생성하여 컨텐트팬의 CENTER에 부착
        makeMenu(); // MenuBar를 생성하여 컨텐트팬에 부착
        makeToolBar(); // JToolBar를 생성하여 아이콘으로 게임 기능 조절
        setResizable(false);
        setVisible(true);
    }

    private void splitPane() {
        // 패널을 둘로 나눠 왼쪽에 gamePanel을 추가시켜준다.
        JSplitPane hPane = new JSplitPane();
        getContentPane().add(hPane, BorderLayout.CENTER);
        hPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        hPane.setDividerLocation(750);
        hPane.setEnabled(false);
        hPane.setLeftComponent(gamePanel);

        // 오른쪽으로 나뉘어진 패널을 다시 반으로 나눠 scorePanel, editPanel을 부착한다.
        JSplitPane pPane = new JSplitPane();
        pPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
        pPane.setDividerLocation(300);
        pPane.setTopComponent(scorePanel);
        pPane.setBottomComponent(editPanel);
        hPane.setRightComponent(pPane);
    }

    private void makeMenu() {
        // 메뉴 바를 만들어준다.
        JMenuBar mBar = new JMenuBar();
        setJMenuBar(mBar);

        // 게임 메뉴
        JMenu fileMenu = new JMenu("게임");
        fileMenu.setBorder(new LineBorder(Color.BLACK));
        fileMenu.add(startItem);
        fileMenu.add(mainItem);
        mBar.add(fileMenu);

        // 정보 메뉴
        JMenu rankMenu = new JMenu("정보");
        rankMenu.setBorder(new LineBorder(Color.BLACK));
        mBar.add(rankMenu);
        rankMenu.add(rankItem);

        // 난이도 조절 메뉴
        JMenu difMenu = new JMenu("난이도");
        difMenu.setBorder(new LineBorder(Color.BLACK));
        mBar.add(difMenu);
        difMenu.add(easyItem);
        difMenu.add(normalItem);
        difMenu.add(hardItem);

        startItem.addActionListener(new StartAction());
        mainItem.addActionListener(new mainAction());
        rankItem.addActionListener(new RankAction());
        easyItem.addActionListener(new easyAction());
        normalItem.addActionListener(new normalAction());
        hardItem.addActionListener(new hardAction());
    }

    private void makeToolBar() {
        JToolBar tBar = new JToolBar();
        tBar.add(startBtn);
        tBar.add(resetBtn);
        tBar.add(rankBtn);
        tBar.add(soundBtn);
        getContentPane().add(tBar, BorderLayout.NORTH);

        startBtn.addActionListener(new StartAction());
        startBtn.setRolloverIcon(overIcon);
        startBtn.setPressedIcon(pressedIcon);

        resetBtn.addActionListener(new ResetAction());
        resetBtn.setRolloverIcon(reset1);
        resetBtn.setPressedIcon(reset1);

        rankBtn.addActionListener(new RankAction());
        soundBtn.addActionListener(new SoundAction());
    }

    private class StartAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            gamePanel.startGame();
            startBtn.setEnabled(false);
        }
    }

    private class ResetAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            gamePanel.restartAudio();
            gamePanel.gameRestart = true;
            dispose();
            new GameFrame();
        }
    }

    private class RankAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            new RankFrame();
        }
    }

    private class mainAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            dispose();
            gamePanel.muteAudio();
            new GameTitleFrame();
        }
    }

    private class easyAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            scorePanel.easyMode();
        }
    }

    private class normalAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            scorePanel.normalMode();
        }
    }

    private class hardAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            scorePanel.hardMode();
        }
    }

    private class SoundAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // isMute가 false이면 눌렀을 때 true로 변경하고 음소거를 한다.
            if (!isMute) {
                isMute = !isMute;
                gamePanel.muteAudio();
                soundBtn.setIcon(mute);
                soundBtn.setRolloverIcon(mute2);

            } else if (isMute) { // 음소거를 풀어준다.
                isMute = !isMute;
                gamePanel.startAudio();
                soundBtn.setIcon(sound);
                soundBtn.setRolloverIcon(sound2);
            }
        }
    }
}
