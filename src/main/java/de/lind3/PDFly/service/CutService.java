package de.lind3.PDFly.service;

import de.lind3.PDFly.utils.PdfUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class CutService {

    public byte[] cutPdfAndMerge(MultipartFile file, String pageNumbers){

        // Only accept pdf files.
        if (!Objects.equals(file.getContentType(), "application/pdf")) {
            throw new IllegalStateException("PDF files only!");
        }

        PDDocument cutDocument = new PDDocument();
        PDDocument document = PdfUtils.convertMultipartFileToPDDocument(file);         // convert MultiPart to PDDocument
        List<Integer> pagesToExtract = PdfUtils.parsePageNumbers(pageNumbers);         // parse Numbers of pageNumbers String.

        for(Integer pageIndex : pagesToExtract){
            PDPage page = document.getPage(pageIndex-1);
            cutDocument.addPage(page);
        }

        return PdfUtils.convertPDDocumentToByteArray(cutDocument);
    }


    public byte[] cutPdf(MultipartFile file, String pageNumbers){
        if (!Objects.equals(file.getContentType(), "application/pdf")) {
            throw new IllegalStateException("PDF files only!");
        }

        PDDocument document = PdfUtils.convertMultipartFileToPDDocument(file);         // convert MultiPart to PDDocument
        List<Integer> pagesToExtract = PdfUtils.parsePageNumbers(pageNumbers);         // parse Numbers of pageNumbers String.
        List<PDDocument> pdfDocuments = new ArrayList<>();

        // Convert every page into a seperate PDF file and add it to Array.
        for(Integer pageIndex : pagesToExtract){
            PDDocument newDocument = new PDDocument();
            PDPage page = document.getPage(pageIndex-1);
            newDocument.addPage(page);
            pdfDocuments.add(newDocument);
        }
        return PdfUtils.convertPDDocumentsToZipByteArray(pdfDocuments, file.getName());
    }


}
