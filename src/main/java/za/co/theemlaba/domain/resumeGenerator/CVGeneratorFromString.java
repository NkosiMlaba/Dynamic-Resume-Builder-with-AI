package za.co.theemlaba.domain.resumeGenerator;

import org.apache.poi.xwpf.usermodel.*;

import za.co.theemlaba.domain.models.Llama3Request;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CVGeneratorFromString {

    public static void main(String[] args) {
        String input = Llama3Request.main(args);
        write(input);

        XWPFDocument document = new XWPFDocument();

        try (FileOutputStream out = new FileOutputStream("CV.docx")) {
            addSection(document, input, "\\*\\*(.*?)\\*\\*", ParagraphAlignment.CENTER, true, 20);
            document.write(out);
            System.out.println("CV created successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void addSection(XWPFDocument document, String input, String regex, ParagraphAlignment alignment, boolean bold, int fontSize) {
        Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            XWPFParagraph paragraph = document.createParagraph();
            XWPFRun run = paragraph.createRun();
            run.setText(matcher.group(1).trim());
            run.setBold(bold);
            run.setFontSize(fontSize);
            paragraph.setAlignment(alignment);
            paragraph.setSpacingAfter(200);
        }
    }

    public static void write(String content) {
        String filePath = "output.txt";

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
}

