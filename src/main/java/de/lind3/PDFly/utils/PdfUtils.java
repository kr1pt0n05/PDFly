package de.lind3.PDFly.utils;

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

    public static PDDocument convertMultipartFileToPDDocument(MultipartFile file){

        try {
            byte[] bytes = file.getBytes();
            return Loader.loadPDF(bytes);

        }catch (IOException e){
            throw new IllegalStateException("Error parsing pdf!");
        }
    }


    public static List<Integer> parsePageNumbers(String input){
        List<Integer> pageNumbers = new ArrayList<>();

        String[] ranges = input.split(",");

        for(String range : ranges){
            range = range.trim(); // remove whitespace

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
        }
        return pageNumbers;
    }


    public static byte[] convertPDDocumentToByteArray(PDDocument pdDocument){
        if(pdDocument == null){
            throw  new IllegalStateException("PDDocument must not be null.");
        }

        try(ByteArrayOutputStream outputStream = new ByteArrayOutputStream()){
            pdDocument.save(outputStream); // Write PDDocument to OutputStream
            pdDocument.close();
            return outputStream.toByteArray();

        } catch (IOException e) {
            throw new IllegalStateException("Error converting Document: " + e.getMessage(), e);
        }
    }


    public static byte[] convertPDDocumentsToZipByteArray(List<PDDocument> pdfDocuments, String fileName){
        try(ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream);
        ){

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

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



}
