/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.vi.VietnameseAnalyzer;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;

/**
 *
 * @author BVKieu
 */
public final class FileServices {

    public static File pdfFilePicker() {
        File file = null;
        //Choose file
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setFileFilter(new FileNameExtensionFilter("PDF files", "pdf"));
        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            file = fileChooser.getSelectedFile();
        }
        return file;
    }

    public static boolean saveSummaryDocument(File file) throws IOException {
        File destFile = new File("documents/summary/" + file.getName());

        Path sourcePath = Paths.get(file.toURI());
        Path destPath = Paths.get(destFile.toURI());
        Files.copy(sourcePath, destPath);
        System.out.println("File copied successfully!");
        return true;

    }

    public static boolean saveFinalDocument(File file) throws IOException {
        File destFile = new File("documents/final/" + file.getName());

        Path sourcePath = Paths.get(file.toURI());
        Path destPath = Paths.get(destFile.toURI());
        Files.copy(sourcePath, destPath);
        System.out.println("File copied successfully!");
        return true;

    }

    public static String fileToTextWithoutSpecialCharacter(File file) throws IOException, TikaException {
        String text = null;
        Tika tika = new Tika();
        text = tika.parseToString(file).replaceAll("[^\\p{L}\\p{N} ]", "");

        return text;
    }
}
