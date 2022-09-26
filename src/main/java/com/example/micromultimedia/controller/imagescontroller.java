package com.example.micromultimedia.controller;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/images")
public class imagescontroller {
    @GetMapping("/getextrapdf")
    @ResponseBody
    public ResponseEntity<Resource> download(@RequestParam("path")String path) {

        return getResourceResponseEntity(path);
    }

    @GetMapping("/getresource")
    @ResponseBody
    public ResponseEntity<Resource> getrecurso(@RequestParam("path")String path){
        return getResourceResponseEntity(path);
    }

    private ResponseEntity<Resource> getResourceResponseEntity(@RequestParam("path") String path) {
        File file= new File(System.getProperty("user.home")+"/ObjetosVirtuales"+path);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.set(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename="+ file.getName());
        InputStreamResource resource = null;
        try {
            resource = new InputStreamResource(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }
    @GetMapping("/Download")
    @ResponseBody
    public ResponseEntity<Resource> getFile(@RequestParam String path) throws FileNotFoundException {
        File file1= new File(System.getProperty("user.home")+"/ObjetosVirtuales"+path);
        Resource file = new InputStreamResource(new FileInputStream(file1));
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment;filename=\"" + file.getFilename() + "\"").body(file);

    }
    @GetMapping("/getvideo")
    public HttpEntity<?> getvideo(@RequestParam("path")String path) {
        //System.out.println(path);
        String home=System.getProperty("user.home");
        File file = new File(home+"/videos/"+path);
        System.out.println(home+"/videos/"+path);

        try {
            byte[] image = Files.readAllBytes(file.toPath());
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentLength(image.length);
            //System.out.println("holie");
            return new HttpEntity<>(image, headers);
        } catch (IOException e) {
            e.printStackTrace();
            return new HttpEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/getimage")
    //@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")

    public HttpEntity<byte[]> getResultado(@RequestParam("path")String path){
        String holi="";
        String userHomeDir = System.getProperty("user.home");
        //System.out.printf("The User Home Directory is %s", userHomeDir);
        System.out.println("*********************+");
        File file=new  File(userHomeDir+"/ObjetosVirtuales"+path);

        System.out.println(userHomeDir+"/ObjetosVirtuales"+path);
        try {
            byte[] image = Files.readAllBytes(file.toPath());
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            headers.setContentLength(image.length);
            return new HttpEntity<>(image,headers);
        } catch (Exception e) {
            e.printStackTrace();
            return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

}
