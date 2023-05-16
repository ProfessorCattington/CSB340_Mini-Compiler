package Lexer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class ProgramRunner {

    public static void main(String[] args) {

        ArrayList<String> files = new ArrayList<String>();
        files.add("additional_input_files/nfactorial.c");
        files.add("additional_input_files/utopiantree.c");

        lexicalAnalysis_multi(files);
    }

    //code that was given. putting it here so I can test stuff in isolation
    public static void lexicalAnalysis_single(String fileName) {

        if (1 == 1) {
            try {

                File f = new File("main\\resources\\" + fileName);
                Scanner s = new Scanner(f);
                String source = " ";
                String result = " ";
                while (s.hasNext()) {
                    source += s.nextLine() + "\n";
                }
                Lexer l = new Lexer(source);
                result = l.printTokens();

                Lexer.outputToFile(result, fileName);

            } catch (FileNotFoundException e) {
                Lexer.error(-1, -1, "Exception: " + e.getMessage());
            }
        } else {
            Lexer.error(-1, -1, "No args");
        }
    }

    //method for handling a large set of files
    public static void lexicalAnalysis_multi(ArrayList<String> fileNames) {

        for (int i = 0; i < fileNames.size(); i++) {

            try {
                String filePath = "src\\main\\resources\\" + fileNames.get(i);
                File f = new File(filePath);

                Scanner s = new Scanner(f);
                String source = " ";
                String result = " ";
                while (s.hasNext()) {
                    source += s.nextLine() + "\n";
                }
                Lexer l = new Lexer(source);
                result = l.printTokens();

                Lexer.outputToFile(fileNames.get(i), result);

            } catch (FileNotFoundException e) {
                Lexer.error(-1, -1, "Exception: " + e.getMessage());
            }
        }
    }
}
