package de.lind3.PDFly.service;

import de.lind3.PDFly.exception.NoFileUploadedException;
import org.apache.pdfbox.io.RandomAccessReadBuffer;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;


@Service
public class MergeService {

    public byte[] mergePdfs(MultipartFile[] files) throws IOException {

        if(files == null || files.length == 0 || Arrays.stream(files).allMatch(MultipartFile::isEmpty)){
            throw new NoFileUploadedException("Oops! It looks like you forgot to upload the documents.");
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
