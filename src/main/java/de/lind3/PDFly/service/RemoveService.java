package de.lind3.PDFly.service;

import de.lind3.PDFly.exception.EmptyDocumentException;
import de.lind3.PDFly.exception.InvalidFileTypeException;
import de.lind3.PDFly.exception.InvalidPageException;
import de.lind3.PDFly.utils.PdfUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Service
public class RemoveService {

    public byte[] removePage(MultipartFile file, String pageNumbers) throws IOException {

        if(file == null || file.isEmpty()){
            throw new EmptyDocumentException("Oops! It looks like you forgot to upload the document.");
        }

        if (!Objects.equals(file.getContentType(), "application/pdf")) {
            throw new InvalidFileTypeException("Invalid file type, PDFs only.");
        }

        PDDocument document = PdfUtils.convertMultipartFileToPDDocument(file);
        List<Integer> pagesToRemove = PdfUtils.parsePageNumbers(pageNumbers);


        try{
            // Need to iterate list from behind, otherwise indexes of pages will change.
            for(int i = pagesToRemove.size(); i > 0; i--){
                document.removePage(i);
            }
        }catch (IndexOutOfBoundsException ex){
            throw new InvalidPageException("Invalid page number(s), please try again.");
        }


        return PdfUtils.convertPDDocumentToByteArray(document);
    }

}
