import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class CountCodeLines {

    private long count(File file) {
        long codeLines = 0L;
        try (Scanner myReader = new Scanner(new FileInputStream(file))) {
            boolean multiLineComment = false;
            boolean lineHadCode = false;
            while (myReader.hasNextLine()) {
                if (lineHadCode) {
                    codeLines++;
                    lineHadCode = false;
                }
                String line = myReader.nextLine();
                if (line.trim().length() == 0) {
                    continue;
                }
                int index = 0;
                boolean textArea = false;
                do {
                    char c = line.charAt(index);
                    if (!multiLineComment && c == '"') {
                        if (index != 0 && line.charAt(index - 1) != '\\') {
                            textArea = !textArea;
                            index++;
                            continue;
                        }
                    }
                    if (textArea) {
                        index++;
                        continue;
                    }
                    if (multiLineComment) {
                        if (c != '*') {
                            index++;
                            continue;
                        } else if (index != line.length() - 1) {
                            char nc = line.charAt(index + 1);
                            if (nc == '/') {
                                multiLineComment = false;
                                index += 2;
                            } else {
                                index++;
                            }
                            continue;
                        } else {
                            break;
                        }
                    }
                    if (c == '/') {
                        if (index != line.length() - 1) {
                            char nc = line.charAt(index + 1);
                            if (nc == '/') {
                                break;
                            }
                            if (nc == '*') {
                                multiLineComment = true;
                                index += 2;
                                continue;
                            }
                        } else {
                            break;
                        }
                    }
                    lineHadCode = true;
                    index++;
                } while (index < line.length());
            }
        } catch (
                FileNotFoundException e) {
            e.printStackTrace();
        }
        return codeLines;
    }

    public static void main(String[] args) {
        CountCodeLines counter = new CountCodeLines();
        System.out.println(counter.count(new File("comment.txt")));
    }
}
