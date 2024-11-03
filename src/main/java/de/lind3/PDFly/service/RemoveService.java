package de.lind3.PDFly.service;

import de.lind3.PDFly.exception.InvalidFileTypeException;
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

        if (!Objects.equals(file.getContentType(), "application/pdf")) {
            throw new InvalidFileTypeException("Invalid file type, PDFs only.");
        }

        PDDocument document = PdfUtils.convertMultipartFileToPDDocument(file);
        List<Integer> pagesToRemove = PdfUtils.parsePageNumbers(pageNumbers);

        // Need to iterate list from behind, otherwise indexes of pages will change.
        for(int i = pagesToRemove.size(); i > 0; i--){
            document.removePage(i);
        }

        return PdfUtils.convertPDDocumentToByteArray(document);
    }

}
