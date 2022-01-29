package br.com.gustavobarbozamarques.controllers;

import br.com.gustavobarbozamarques.services.S3Service;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(S3Controller.class)
public class S3ControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private S3Service service;

    @Test
    void testListShouldReturnSuccess() throws Exception {
        var files = List.of("file-1.txt", "file-2.txt", "file-2.txt");
        when(service.list()).thenReturn(files);
        this.mockMvc
                .perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value(files.get(0)));
    }

    @Test
    void testDownloadReturnSuccess() throws Exception {
        String key = "file-1.txt";
        this.mockMvc
                .perform(get("/download").param("key", key))
                .andDo(print())
                .andExpect(status().isOk());
        verify(service, times(1)).download(key);
    }

    @Test
    void testDownloadWhenMissingKeyShouldReturnBadRequest() throws Exception {
        this.mockMvc
                .perform(get("/download"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void testDeleteShouldReturnSuccess() throws Exception {
        String key = "file-1.txt";
        this.mockMvc
                .perform(delete("/").param("key", key))
                .andDo(print())
                .andExpect(status().isOk());
        verify(service, times(1)).delete(key);
    }

    @Test
    void testDeleteWhenMissingKeyShouldReturnBadRequest() throws Exception {
        this.mockMvc
                .perform(delete("/"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGenerateTempPublicLinkShouldReturnSuccess() throws Exception {
        String key = "file-1.txt";
        String link = "https://bucket.aws.com/file-1.txt";
        when(service.generateTempPublicLink(key)).thenReturn(link);
        this.mockMvc
                .perform(get("/link").param("key", key))
                .andDo(print())
                .andExpect(content().string(link))
                .andExpect(status().isOk());
        verify(service, times(1)).generateTempPublicLink(key);
    }

    @Test
    void testGenerateTempPublicLinkWhenMissingKeyShouldReturnBadRequest() throws Exception {
        this.mockMvc
                .perform(get("/link"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUploadShouldReturnSuccess() throws Exception {
        mockMvc.perform(multipart("/").file("file", "file content".getBytes()))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void testUploadWhenMissingFileShouldReturnBadRequest() throws Exception {
        mockMvc.perform(multipart("/").file("INVALID_NAME", null))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}
