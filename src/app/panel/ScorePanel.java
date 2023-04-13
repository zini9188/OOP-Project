package app.panel;

import javax.swing.*;
import java.awt.*;

public class ScorePanel extends JPanel {
    private int score = 0;
    private int incScore = 100;
    private final JLabel textLabel = new JLabel("점수");
    private final JLabel speedLabel = new JLabel("느림 속도 조절 빠름");
    private final JLabel periodLabel = new JLabel("빠름 주기 조절 느림");
    private final JLabel colorLabel = new JLabel(""); // 색이 있는 미사일 맞추면 기능 알려주는 레이블
    private final JSlider fallSpeedSlider = new JSlider(JSlider.HORIZONTAL, 5, 30, 5);
    private final JSlider periodSlider = new JSlider(JSlider.HORIZONTAL, 1, 10, 2);
    private final JLabel scoreLabel = new JLabel(Integer.toString(score));
    private final Font font = new Font(null, Font.BOLD, 20);

    public ScorePanel() {
        this.setBackground(Color.lightGray);
        setLayout(null);
        textLabel.setBounds(65, 10, 50, 20);
        textLabel.setFont(font);
        scoreLabel.setBounds(75, 40, 100, 20);
        scoreLabel.setFont(font);
        speedLabel.setBounds(10, 90, 200, 20);
        fallSpeedSlider.setBounds(10, 110, 150, 50);
        fallSpeedSlider.setBackground(Color.lightGray);
        fallSpeedSlider.setForeground(Color.BLACK);
        fallSpeedSlider.setPaintLabels(true);
        fallSpeedSlider.setPaintTicks(true);
        fallSpeedSlider.setPaintTrack(true);
        fallSpeedSlider.setMajorTickSpacing(4);
        fallSpeedSlider.setMinorTickSpacing(1);

        periodLabel.setBounds(10, 160, 200, 20);
        periodSlider.setBounds(10, 180, 150, 50);
        periodSlider.setBackground(Color.lightGray);
        periodSlider.setForeground(Color.BLACK);
        periodSlider.setPaintLabels(true);
        periodSlider.setPaintTicks(true);
        periodSlider.setPaintTrack(true);
        periodSlider.setMajorTickSpacing(5);
        periodSlider.setMinorTickSpacing(1);

        colorLabel.setBounds(10, 230, 200, 30);
        colorLabel.setFont(font);

        add(textLabel);
        add(scoreLabel);
        add(speedLabel);
        add(fallSpeedSlider);
        add(periodLabel);
        add(periodSlider);
        add(colorLabel);
    }

    public void setSlider(int speed) {
        fallSpeedSlider.setValue(speed);
    }

    public void setTextColorLabel(String text) {
        colorLabel.setText(text);
    }

    public int getScore() {
        return score;
    }

    public int getSpeed() {
        return fallSpeedSlider.getValue();
    }

    public int getPeriod() {
        return periodSlider.getValue() * 1000;
    }

    public void increase() {
        score += incScore;
        scoreLabel.setText
                (Integer.toString(score));
    }

    public void easyMode() {
        fallSpeedSlider.setValue(5);
        periodSlider.setValue(5);
    }

    public void normalMode() {
        fallSpeedSlider.setValue(10);
        periodSlider.setValue(3);
    }

    public void hardMode() {
        fallSpeedSlider.setValue(15);
        periodSlider.setValue(1);
    }

    public void setIncScore(int n) {
        incScore = n;
    }
}