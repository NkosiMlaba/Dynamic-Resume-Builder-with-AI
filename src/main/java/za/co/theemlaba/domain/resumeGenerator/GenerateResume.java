package za.co.theemlaba.domain.resumeGenerator;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

public class GenerateResume {
    String filePath = "src/main/resources/resumes/";

    public void generateCV(String email, String input) {
        createUserDirectory(email);
        input = removeMessageBeforeColon(input);
        generateDocument(email, input);
        generateTxt(email, input);
        generatePDF(email, input);
    }

    public void generateDocument(String email, String input) {
        XWPFDocument document = new XWPFDocument();
        try (FileOutputStream out = new FileOutputStream(filePath + email + "/resume.docx")) {
            processDocument(document, input);
            document.write(out);
            System.out.println("Word CV created successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void generateTxt(String email, String input) {
        try (FileWriter writer = new FileWriter(filePath + email + "/resume.txt")) {
            writer.write(input);
            System.out.println("Text CV created successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void generatePDF(String email, String input) {
        Document document = new Document();
        try (FileOutputStream out = new FileOutputStream(filePath + email + "/resume.pdf")) {
            PdfWriter.getInstance(document, out);
            document.open();
            document.add(new Paragraph(input));
            document.close();
            System.out.println("PDF CV created successfully.");
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }
    }

    // Utility method to create user directory if it does not exist
    public void createUserDirectory(String email) {
        File directory = new File(filePath + email);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    // Utility method to process and format the Word document content
    private static void processDocument(XWPFDocument document, String input) {
        String[] lines = input.split("\n");
        XWPFParagraph paragraph;

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

            } else if (line.trim().startsWith("+ ")) {
                String indentBullet = line.replace("+ ", "◦ ").trim();
                paragraph = document.createParagraph();
                XWPFRun run = paragraph.createRun();
                run.setText(indentBullet.trim());
                run.setFontSize(8);
                paragraph.setSpacingAfter(100);
                paragraph.setIndentationFirstLine(1000);

            } else if (!line.trim().isEmpty()) {
                paragraph = document.createParagraph();
                XWPFRun run = paragraph.createRun();
                run.setText(line.trim());
                run.setFontSize(8);
                paragraph.setSpacingAfter(100);
            }
        }
    }

    // Remove everything before the colon for formatting purposes
    private static String removeMessageBeforeColon(String input) {
        int colonIndex = input.indexOf(":\n\n**");
        if (colonIndex != -1 && colonIndex + 1 < input.length()) {
            return input.substring(colonIndex + 1).trim();
        }
        return input;
    }
}
