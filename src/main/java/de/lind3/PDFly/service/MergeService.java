package de.lind3.PDFly.service;

import de.lind3.PDFly.exception.NoFileUploadedException;
import de.lind3.PDFly.utils.PdfUtils;
import org.apache.pdfbox.io.RandomAccessRead;
import org.apache.pdfbox.io.RandomAccessReadBuffer;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class MergeService {

    public byte[] mergePdfs(MultipartFile[] files) throws IOException {

        if(files.length == 0){
            throw new NoFileUploadedException("No files uploaded");
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PDFMergerUtility pdfMerger = new PDFMergerUtility();
        pdfMerger.setDestinationStream(outputStream);

        for(MultipartFile file : files){
            RandomAccessReadBuffer buffer = new RandomAccessReadBuffer(file.getBytes());
            pdfMerger.addSource(buffer);
        }
        pdfMerger.mergeDocuments(null);

        return outputStream.toByteArray();
    }


}
