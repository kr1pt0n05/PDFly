package de.lind3.PDFly.utils;

import de.lind3.PDFly.exception.EmptyDocumentException;
import de.lind3.PDFly.exception.InvalidCharactersException;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class PdfUtils {

    public static PDDocument convertMultipartFileToPDDocument(MultipartFile file) throws IOException {
        byte[] bytes = file.getBytes();
        return Loader.loadPDF(bytes);

    }

    public static List<Integer> parsePageNumbers(String input){
        List<Integer> pageNumbers = new ArrayList<>();

        String[] ranges = input.split(",");

        for(String range : ranges){
            range = range.trim(); // remove whitespace

            try{
                if(range.contains("-")){
                    // Range found, split by hyphen and parse start and end
                    String[] bounds = range.split("-");

                    int start = Integer.parseInt(bounds[0].trim());
                    int end = Integer.parseInt(bounds[1].trim());


                    for(int i = start; i < end+1; i++){
                        pageNumbers.add(i);
                    }
                }else{
                    // Single number, parse and add to list
                    pageNumbers.add(Integer.parseInt(range));
                }

            }catch(NumberFormatException ex){
                throw new InvalidCharactersException("Specified pages contained invalid characters.");
            }

        }
        return pageNumbers;
    }


    public static byte[] convertPDDocumentToByteArray(PDDocument pdDocument) throws IOException {
        if(pdDocument == null){
            throw  new EmptyDocumentException("Didn't provide a Pdf file.");
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        pdDocument.save(outputStream); // Write PDDocument to OutputStream
        pdDocument.close();
        return outputStream.toByteArray();
    }


    public static byte[] convertPDDocumentsToZipByteArray(List<PDDocument> pdfDocuments, String fileName) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream);

        for(int i = 0; i < pdfDocuments.size(); i++){
            PDDocument document = pdfDocuments.get(i);
            String zipEntryName = fileName + (i+1) + ".pdf";

            ZipEntry zipEntry = new ZipEntry(zipEntryName);
            zipOutputStream.putNextEntry(zipEntry);

            document.save(zipOutputStream);
            zipOutputStream.closeEntry();
            document.close();
        }
        zipOutputStream.finish();
        return byteArrayOutputStream.toByteArray();
    }


}
