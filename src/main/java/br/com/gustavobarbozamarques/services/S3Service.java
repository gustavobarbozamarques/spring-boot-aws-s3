package br.com.gustavobarbozamarques.services;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class S3Service {

    @Value("${s3.bucket}")
    private String bucket;

    @Value("${s3.link.max.minutes.expiration.time}")
    private Long linkMaxExpirationTime;

    @Autowired
    private AmazonS3 s3;

    public List<String> list() {
        return s3.listObjects(bucket)
                .getObjectSummaries()
                .stream()
                .map(object -> object.getKey())
                .collect(Collectors.toList());
    }

    public void delete(String key) {
        s3.deleteObject(bucket, key);
    }


    public byte[] download(String key) throws IOException {
        return IOUtils.toByteArray(s3.getObject(bucket, key).getObjectContent());
    }

    public String generateTempPublicLink(String key) {
        Date linkMaxExpirationTimeDate = Date.from(LocalDateTime.now().plusMinutes(linkMaxExpirationTime).atZone(ZoneId.systemDefault()).toInstant());
        return s3.generatePresignedUrl(bucket, key, linkMaxExpirationTimeDate, HttpMethod.GET).toString();
    }

    public void upload(MultipartFile multipartFile) throws IOException {
        File file = convertMultiPartToFile(multipartFile);
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, multipartFile.getOriginalFilename(), file);
        s3.putObject(putObjectRequest);
        file.delete();
    }

    private File convertMultiPartToFile(MultipartFile multipartFile) throws IOException {
        File file = new File(multipartFile.getOriginalFilename());
        try (OutputStream os = new FileOutputStream(file)) {
            os.write(multipartFile.getBytes());
        }
        return file;
    }
}
