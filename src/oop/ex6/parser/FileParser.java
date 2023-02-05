package oop.ex6.parser;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * FileParser class, reads the files and inserts it lines into a List of Strings
 */
public class FileParser {


    private final List<String> fileContent;
    private int curLine;

    /**
     * Class constructor
     * @param filePath The file path for the file we want to read
     * @throws IOException Exception thrown when there is an I/O error
     */
    public FileParser(String filePath) throws IOException {
        this.fileContent = parseFileContent(filePath);
        this.curLine = 0;
    }

    /**
     * The function parses through the file and saves its lines
     * @param filePath The file path for the file we want to read
     * @return a list of Strings which contains the files content
     * @throws IOException Exception thrown when there is an I/O error
     */
    private List<String> parseFileContent(String filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            List<String> lines = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                if (isLineHasCode(line)) {
                    lines.add(line);
                }
            }
            return lines;
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        }
    }

    /**
     * The function checks whether the line contains code
     * @param line The line which we want to check
     * @return true if the line is not an empty line or comment, false otherwise
     */
    private boolean isLineHasCode(String line) {
        return !line.trim().isEmpty() && !line.startsWith("//");
    }

    /**
     * Getter function for the file content
     * @return the file content
     */
    public List<String> getFileContent() {
        return fileContent;
    }
}

