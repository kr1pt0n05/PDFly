package de.lind3.PDFly.service;

import de.lind3.PDFly.exception.EmptyDocumentException;
import de.lind3.PDFly.exception.InvalidFileTypeException;
import de.lind3.PDFly.exception.InvalidPageException;
import de.lind3.PDFly.utils.PdfUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class CutService {

    public byte[] cutPdfAndMerge(MultipartFile file, String pageNumbers) throws IOException {

        if(file == null || file.isEmpty()){
            throw new EmptyDocumentException("Oops! It looks like you forgot to upload the document.");
        }
        // Only accept pdf files.
        if (!Objects.equals(file.getContentType(), "application/pdf")) {
            throw new InvalidFileTypeException("Invalid file type, PDFs only.");
        }

        PDDocument cutDocument = new PDDocument();
        PDDocument document = PdfUtils.convertMultipartFileToPDDocument(file);         // convert MultiPart to PDDocument
        List<Integer> pagesToExtract = PdfUtils.parsePageNumbers(pageNumbers);         // parse Numbers of pageNumbers String.

        try{
            for(Integer pageIndex : pagesToExtract){
                PDPage page = document.getPage(pageIndex-1);
                cutDocument.addPage(page);
            }
        }catch (IndexOutOfBoundsException ex){
            throw new InvalidPageException("Invalid page number(s), please try again.");
        }

        return PdfUtils.convertPDDocumentToByteArray(cutDocument);
    }


    public byte[] cutPdf(MultipartFile file, String pageNumbers) throws IOException {

        if(file == null || file.isEmpty()){
            throw new EmptyDocumentException("Oops! It looks like you forgot to upload the document.");
        }

        if (!Objects.equals(file.getContentType(), "application/pdf")) {
            throw new IllegalStateException("PDF files only!");
        }

        PDDocument document = PdfUtils.convertMultipartFileToPDDocument(file);         // convert MultiPart to PDDocument
        List<Integer> pagesToExtract = PdfUtils.parsePageNumbers(pageNumbers);         // parse Numbers of pageNumbers String.
        List<PDDocument> pdfDocuments = new ArrayList<>();

        try{
            // Convert every page into a seperate PDF file and add it to Array.
            for(Integer pageIndex : pagesToExtract){
                PDDocument newDocument = new PDDocument();
                PDPage page = document.getPage(pageIndex-1);
                newDocument.addPage(page);
                pdfDocuments.add(newDocument);
            }
        }catch (IndexOutOfBoundsException ex){
            throw new InvalidPageException("Invalid page number(s), please try again.");
        }

        return PdfUtils.convertPDDocumentsToZipByteArray(pdfDocuments, file.getName());
    }


}
