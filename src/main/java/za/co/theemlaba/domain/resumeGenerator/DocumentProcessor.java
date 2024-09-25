package za.co.theemlaba.domain.resumeGenerator;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileInputStream;

public class DocumentProcessor {
    
    public static String removeMessageBeforeColon(String input) {
        int colonIndex = input.indexOf(":\n\n**");
        if (colonIndex != -1 && colonIndex + 1 < input.length()) {
            return input.substring(colonIndex + 1).trim();
        }
        return input;
    }

    public void generateTxt(String path, String input) {
        try (FileWriter writer = new FileWriter(path)) {
            writer.write(input);
            System.out.println("Text CV created successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void generatePDF(String path, String input) {
        Document document = new Document();
        try (FileOutputStream out = new FileOutputStream(path)) {
            PdfWriter.getInstance(document, out);
            document.open();
            document.add(new Paragraph(input));
            document.close();
            System.out.println("PDF CV created successfully.");
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }
    }

    public void convertDocxToTxt(String input, String output) {
        try (FileInputStream fis = new FileInputStream(input);
            
             FileWriter writer = new FileWriter(output)) {
            XWPFDocument document = new XWPFDocument(fis);
            String text = extractTextFromDocx(document);
            writer.write(text);
            System.out.println("DOCX converted to TXT successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String extractTextFromDocx(XWPFDocument document) {
        StringBuilder text = new StringBuilder();
        for (XWPFParagraph paragraph : document.getParagraphs()) {
            text.append(paragraph.getText()).append("\n");
        }
        return text.toString();
    }

    public void convertDocxToPdf(String input, String output) {
        try (FileInputStream fis = new FileInputStream(input);
            FileOutputStream out = new FileOutputStream(output)) {
            XWPFDocument document = new XWPFDocument(fis);
            Document pdfDoc = new Document();
            PdfWriter.getInstance(pdfDoc, out);
            pdfDoc.open();
            for (XWPFParagraph para : document.getParagraphs()) {
                pdfDoc.add(new Paragraph(para.getText()));
            }
            pdfDoc.close();
            System.out.println("DOCX converted to PDF successfully.");
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }
    }

    public void createUserDirectory(String path) {
        File directory = new File(path);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }
}
