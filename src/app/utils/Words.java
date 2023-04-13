package app.utils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.Vector;

public class Words {
    private final Vector<String> words = new Vector<>(50000);

    public Words(String fileName) {
        // 파일에서 읽기
        try {
            Scanner scanner = new Scanner(new FileReader(fileName));
            while (scanner.hasNext()) {
                String word = scanner.nextLine();
                words.add(word);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("file not found error");
            System.exit(0);
        }
    }

    // 단어를 랜덤하게 배정한다.
    public String getRandomWord() {
        final int maxWord = words.size();
        int index = (int) (Math.random() * maxWord);
        return words.get(index);
    }

    public Vector<String> getAllWord() {
        return words;
    }

    public static void addWord(String fileName, String word) {
        try {
            FileWriter fw = new FileWriter(fileName, true);
            fw.write(word);
            fw.write("\r\n");
            fw.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}