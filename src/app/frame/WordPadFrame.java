package app.frame;

import app.utils.Words;

import javax.swing.*;

public class WordPadFrame extends JFrame {

    public WordPadFrame() {
        super();
        setLayout(null);
        setBounds(1200, 500, 300, 400);
        JLabel lb = new JLabel("단어 리스트");
        lb.setBounds(110, 30, 80, 20);
        add(lb);
        // 리스트 형태로 단어들을 보여준다.
        Words word = new Words("txt/words.txt");
        JList<String> scrollList = new JList<>(word.getAllWord());
        JScrollPane scrollPane = new JScrollPane(scrollList);
        scrollPane.setBounds(70, 70, 150, 200);
        JButton closeBtn = new JButton("닫기");
        closeBtn.setBounds(100, 300, 80, 20);
        closeBtn.addActionListener(e -> dispose());
        add(scrollPane);
        add(closeBtn);
        setResizable(false);
        setVisible(true);
    }
}