package Controller;

import static Model.OpenFile.readFile;

public class Controller {
    public static void main(String[] args) {
        System.out.println("Reading source code...");

        String code = readFile();

        if (code != null) {
            System.out.println("=== Source Code ===");
            System.out.println(code);
        } else {
            System.out.println("Failed to read the file.");
        }
    }
}