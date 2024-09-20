package za.co.theemlaba.domain.resumeGenerator;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import java.io.File;


public class GenerateResume {
    String filePath = "src/main/resources/resumes/";

    public void generateCV(String email, String input) {
        createUserDirectory(email);
        input = removeMessageBeforeColon(input);
        generateDocument(email, input);
    }

    public void generateDocument (String email, String input) {
        XWPFDocument document = new XWPFDocument();
        try (FileOutputStream out = new FileOutputStream(filePath + email + "/resume.docx")) {
            processDocument(document, input);
            document.write(out);
            System.out.println("CV created successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createUserDirectory(String email) {
        File directory = new File(filePath + email);
        if (!directory.exists()) {
            directory.mkdirs();
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

    public void write(String content) {
        try {
            FileWriter writer = new FileWriter(filePath);
            writer.write(content);
            writer.close();
            System.out.println("Successfully wrote to the file: " + filePath);
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file: " + filePath);
            e.printStackTrace();
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
