package app.panel;

import app.frame.WordPadFrame;
import app.utils.Words;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EditPanel extends JPanel {
    private final JTextField edit = new JTextField(15);
    private final JButton addButton = new JButton("단어 추가");
    private final JButton viewButton = new JButton("단어 리스트 보기");

    public EditPanel() {
        this.setBackground(Color.lightGray);
        this.setLayout(new FlowLayout());
        add(edit);
        add(addButton);
        add(viewButton);
        addButton.addActionListener(new addAction());
        viewButton.addActionListener(new viewAction());
    }

    // addButton의 actionListener 버튼 클릭시 단어를 추가한다.
    private class addAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (!edit.getText().equals("")) { // 공백만 입력하면 작동하지 않는다.
                String name = edit.getText(); // edit에서 단어를 가져온다.
                edit.setText(""); // edit을 공백으로 바꿔준다.
                Words.addWord("txt/words.txt", name); // 단어장에 가져온 단어를 추가한다.
            }
        }
    }

    // viewButton의 actionListener 버튼 클릭시 단어 리스트를 보여준다.
    private static class viewAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // 단어장 프레임을 화면에 보여준다.
            new WordPadFrame();
        }
    }
}
