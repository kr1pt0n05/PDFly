package de.lind3.PDFly.controller;

import de.lind3.PDFly.exception.NoFileUploadedException;
import de.lind3.PDFly.service.CutService;
import de.lind3.PDFly.service.MergeService;
import de.lind3.PDFly.service.RemoveService;
import de.lind3.PDFly.utils.PdfUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
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
import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping("")
public class PdfController {
    private final CutService cutService;
    private final MergeService mergeService;
    private final RemoveService removeService;

    @Autowired
    public PdfController(CutService cutService, MergeService mergeService, RemoveService removeService) {
        this.cutService = cutService;
        this.mergeService = mergeService;
        this.removeService = removeService;
    }

    @GetMapping
    public String cut(){
        return "cut.html";
    }
    @GetMapping("/merge")
    public String merge(){
        return "merge.html";
    }
    @GetMapping("/remove")
    public String remove(){
        return "remove.html";
    }


    @PostMapping
    public ResponseEntity<byte[]> cutPdf(
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


    @PostMapping("/merge")
    public ResponseEntity<byte[]> mergePdfs(
            @RequestParam("files") MultipartFile[] files
    ) throws IOException {
        byte[] bytes = mergeService.mergePdfs(files);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=merged.pdf");
        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
    }


    @PostMapping("/remove")
    public ResponseEntity<byte[]> removePage(
            @RequestParam("file") MultipartFile file,
            @RequestParam("pageNumbers") String pageNumbers
    ) throws IOException {
        byte[] bytes = removeService.removePage(file, pageNumbers);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=removed.pdf");
        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
    }


}
