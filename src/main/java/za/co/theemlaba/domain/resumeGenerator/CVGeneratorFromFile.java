package za.co.theemlaba.domain.resumeGenerator;

import org.apache.poi.xwpf.usermodel.*;
import java.io.*;
import java.nio.file.*;

public class CVGeneratorFromFile {
    
    public static void main(String[] args) {
        String input = readFile("output.txt");
        input = removeMessageBeforeColon(input);
        XWPFDocument document = new XWPFDocument();

        try (FileOutputStream out = new FileOutputStream("GeneratedCV.docx")) {
            processDocument(document, input);
            document.write(out);
            System.out.println("CV created successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String readFile(String filePath) {
        try {
            return new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void processDocument(XWPFDocument document, String input) {
        String[] lines = input.split("\n");
        XWPFParagraph paragraph = null;

        for (String line : lines) {
            if (line.startsWith("**") && line.endsWith("**")) {
                String header = line.replace("**", "").trim();
                paragraph = document.createParagraph();
                XWPFRun run = paragraph.createRun();
                run.addBreak();
                run.setText(header);
                run.setBold(true);
                run.setFontSize(12);
                paragraph.setSpacingAfter(200);
                paragraph.setAlignment(ParagraphAlignment.LEFT);
                
            } else if (line.startsWith("* ")) {
                String bullet = line.replace("* ", "• ").trim();
                paragraph = document.createParagraph();
                XWPFRun run = paragraph.createRun();
                run.setText(bullet);
                run.setFontSize(8);
                paragraph.setSpacingAfter(100);
                paragraph.setIndentationFirstLine(500);
            } 
            else if (line.trim().startsWith("+ ")) {
                String indentBullet = line.replace("+ ", "◦ ").trim();
                paragraph = document.createParagraph();
                XWPFRun run = paragraph.createRun();
                run.setText(indentBullet.trim());
                run.setFontSize(8);
                paragraph.setSpacingAfter(100);
                paragraph.setIndentationFirstLine(1000);
            }
            else if (!line.trim().isEmpty()) {
                paragraph = document.createParagraph();
                XWPFRun run = paragraph.createRun();
                run.setText(line.trim());
                run.setFontSize(8);
                paragraph.setSpacingAfter(100);
            }
        }
    }

    private static String removeMessageBeforeColon(String input) {
        int colonIndex = input.indexOf(":\n\n**");
        if (colonIndex != -1 && colonIndex + 1 < input.length()) {
            return input.substring(colonIndex + 1).trim();
        }
        return input;
    }
}
