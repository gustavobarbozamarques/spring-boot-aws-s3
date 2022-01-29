package br.com.gustavobarbozamarques.controllers;

import br.com.gustavobarbozamarques.services.S3Service;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

@RestController
@Validated
@Api(tags = "S3 Controller")
public class S3Controller {

    @Autowired
    private S3Service service;

    @GetMapping
    @ApiOperation("Get all file names")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success")
    })
    public List<String> list() {
        return service.list();
    }

    @GetMapping("/download")
    @ApiOperation("Download file")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success")
    })
    public ResponseEntity download(@ApiParam("The file (Object Key)") @RequestParam("key") @NotBlank String key) throws IOException {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + Paths.get(key).getFileName().toString())
                .body(service.download(key));
    }

    @DeleteMapping
    @ApiOperation("Delete file")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success")
    })
    public void delete(@ApiParam("The file (Object Key)") @RequestParam("key") @NotBlank String key) {
        service.delete(key);
    }

    @GetMapping("/link")
    @ApiOperation("Generate temp link")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success")
    })
    public String generateTempPublicLink(@ApiParam("The file (Object Key)") @RequestParam("key") @NotBlank String key) {
        return service.generateTempPublicLink(key);
    }

    @PostMapping
    @ApiOperation("Upload file")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success")
    })
    public void upload(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        service.upload(multipartFile);
    }
}
