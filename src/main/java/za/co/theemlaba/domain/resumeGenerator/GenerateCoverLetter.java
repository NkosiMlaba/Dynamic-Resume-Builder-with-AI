package za.co.theemlaba.domain.resumeGenerator;

import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import java.io.FileOutputStream;
import java.io.IOException;

public class GenerateCoverLetter extends DocumentProcessor {
    String filePath = "src/main/resources/resumes/";

    public void generateCoverLetter(String email, String input) {
        String userPath = filePath + email;
        String docxPath = filePath + email + "/coverletter.docx";
        String pdfPath = filePath + email + "/coverletter.pdf";
        String txtPath = filePath + email + "/coverletter.txt";
        createUserDirectory(userPath);
        input = super.removeMessageBeforeColon(input);
        generateDocument(email, input);
        super.convertDocxToTxt(docxPath, txtPath);
        super.convertDocxToPdf(docxPath, pdfPath);
    }

    public void generateDocument(String email, String input) {
        XWPFDocument document = new XWPFDocument();
        try (FileOutputStream out = new FileOutputStream(filePath + email + "/coverletter.docx")) {
            processDocument(document, input);
            document.write(out);
            System.out.println("Word cover letter created successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
}
