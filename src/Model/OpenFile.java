package Model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class OpenFile {


    public static String readFile() {
        StringBuilder content = new StringBuilder();
        File file = new File("C:\\Users\\Saira\\OneDrive\\Desktop\\Sample source code.txt");

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                content.append(scanner.nextLine()).append("\n");
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error: File 'Sample source code.txt' not found!");
            e.printStackTrace();
            return null;
        }
        return content.toString();
    }
}