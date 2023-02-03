package oop.ex6.parser;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileParser {


    private final List<String> fileContent;
    private int curLine;

    public FileParser(String filePath) throws IOException {
        this.fileContent = parseFileContent(filePath);
        this.curLine = 0;
    }

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

    private boolean isLineHasCode(String line) {
        return !line.trim().isEmpty() && !line.startsWith("//");
    }

    public List<String> getFileContent() {
        return fileContent;
    }
}

