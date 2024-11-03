package de.lind3.PDFly.controller;

import de.lind3.PDFly.service.CutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@Controller
@RequestMapping("")
public class PdfController {
    private final CutService cutService;

    @Autowired
    public PdfController(CutService cutService) {
        this.cutService = cutService;
    }

    @GetMapping
    public String cut(){
        return "cut.html";
    }

    @PostMapping
    public ResponseEntity<byte[]> downloadPdf(
            @RequestParam("file") MultipartFile file,
            @RequestParam("pageNumbers") String pageNumbers,
            @RequestParam(value = "mergeFiles", required = false) String mergePages
    ) throws IOException {
        // Check if checkBox for merging pages was selected.
        Boolean merge = mergePages != null ? Boolean.TRUE : Boolean.FALSE;
        byte[] bytes;
        String filename = file.getName();

        if(merge){
           bytes  = cutService.cutPdfAndMerge(file, pageNumbers);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", String.format("attachment; filename=%s-cut.pdf", filename));
            return new ResponseEntity<>(bytes, headers, HttpStatus.OK);

        }else{
            bytes = cutService.cutPdf(file, pageNumbers);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", String.format("attachment; filename=%s-cut.zip", filename));
            return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
        }
    }


}
