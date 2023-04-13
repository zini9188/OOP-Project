package app.frame;

import javax.swing.*;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class RankFrame extends JFrame {
    // 랭킹을 표시하는 label들의 공통 좌표
    private final int yLabel = 15;
    private final int width = 100;
    private final int height = 50;
    private int rank;
    private JLabel rankLabel, nameLabel, scoreLabel;
    private final ArrayList<String> list = new ArrayList<>();

    public RankFrame() {
        setTitle("Top 10 Ranking");
        setLayout(null);
        setBounds(1200, 500, 400, 400);
        getRank("txt/score.txt");

        JButton closeBtn = new JButton("닫기");
        closeBtn.setBounds(150, 320, 70, 20);
        add(closeBtn);
        closeBtn.addActionListener(e -> dispose());
        setResizable(false);
        setVisible(true);
    }

    private void getRank(String fileName) {
        rankTitle();
        // 파일이 없으면 새로 만들고 있으면 불러온다.
        try {
            File file = new File(fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            Scanner scanner = new Scanner(new FileReader(fileName));
            while (scanner.hasNext()) {
                String rnk = scanner.nextLine();
                list.add(rnk);
            }
            scanner.close();
        } catch (IOException e) {
            System.out.println("File Open Error");
        }

        // 정렬하여 ArrayList에 점수 저장
        ArrayList<Integer> listScore = new ArrayList<>();
        for (int i = 1; i <= list.size() / 2; i++) {
            listScore.add(Integer.valueOf(list.get(2 * i - 1)));
        }

        Collections.sort(listScore);
        ArrayList<String> listScore2 = new ArrayList<>();
        for (Integer integer : listScore) {
            listScore2.add(String.valueOf(integer));
        }

        rank = 0;
        // list에 listScore2와 같은 점수의 인덱스를 가져오고 - 1 한 부분이 이름.
        for (int i = listScore2.size(); i >= 1; i--) {
            int x = list.indexOf(listScore2.get(i - 1));
            rank++;
            if (rank > 10)
                break;
            rankList(x, rank);
        }
    }

    private void rankTitle() {
        rankLabel = new JLabel("순위");
        rankLabel.setBounds(70, yLabel, width, height);
        add(rankLabel);

        nameLabel = new JLabel("이름");
        nameLabel.setBounds(140, yLabel, width, height);
        add(nameLabel);

        scoreLabel = new JLabel("점수");
        scoreLabel.setBounds(240, yLabel, width, height);
        add(scoreLabel);
    }

    private void rankList(int x, int rank) {
        nameLabel = new JLabel(list.get(x - 1));
        nameLabel.setBounds(140, yLabel + 25 * rank, width, height);
        add(nameLabel);

        scoreLabel = new JLabel(list.get(x));
        scoreLabel.setBounds(240, yLabel + 25 * rank, width, height);
        add(scoreLabel);

        rankLabel = new JLabel(Integer.toString(rank));
        rankLabel.setBounds(70, yLabel + 25 * rank, width, height);
        add(rankLabel);
    }
}