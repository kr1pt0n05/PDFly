package de.lind3.PDFly.service;

import de.lind3.PDFly.exception.NoFileUploadedException;
import de.lind3.PDFly.utils.PdfUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class MergeService {

    public byte[] mergePdfs(MultipartFile[] files) throws IOException {

        if(files.length == 0){
            throw new NoFileUploadedException("No files uploaded");
        }

        List<PDDocument> pdfDocuments = new ArrayList<>();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        for(MultipartFile file : files){
            PDDocument document = PdfUtils.convertMultipartFileToPDDocument(file);
            pdfDocuments.add(document);
        }

        for(PDDocument document : pdfDocuments){
            document.save(outputStream);
        }
        outputStream.close();

        return outputStream.toByteArray();
    }


}
