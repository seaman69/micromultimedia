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
    @GetMapping("/getimageszip")
    @ResponseBody
    public ResponseEntity<Resource> download(@RequestParam("path")String path) {
        System.out.println("inicio_compresion");

        System.out.println("fin_compresion");
        System.out.println(System.getProperty("user.home")+"/ObjetosVirtuales"+path);
        File file= new File(System.getProperty("user.home")+"/ObjetosVirtuales"+path+"framesimages.zip");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        InputStreamResource resource = null;
        try {
            resource = new InputStreamResource(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }

    public void pack(String sourceDirPath, String zipFilePath) throws IOException {
        Path p = Files.createFile(Paths.get(zipFilePath));
        try (ZipOutputStream zs = new ZipOutputStream(Files.newOutputStream(p))) {
            Path pp = Paths.get(sourceDirPath);
            Files.walk(pp)
                    .filter(path -> !Files.isDirectory(path))
                    .forEach(path -> {
                        ZipEntry zipEntry = new ZipEntry(pp.relativize(path).toString());
                        try {
                            zs.putNextEntry(zipEntry);
                            Files.copy(path, zs);
                            zs.closeEntry();
                        } catch (IOException e) {
                            System.err.println(e);
                        }
                    });
        }

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
