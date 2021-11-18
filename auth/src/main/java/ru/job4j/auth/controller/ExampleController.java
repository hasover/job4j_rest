package ru.job4j.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMapAdapter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class ExampleController {
    @GetMapping("/example1")
    public ResponseEntity<?> example1() {
        return ResponseEntity.ok("Example1");
    }

    @GetMapping("/example2")
    public ResponseEntity<Map<String, String>> example2() {
        return ResponseEntity.of(Optional.of(new HashMap<>() {{
            put("key", "value");
        }}));

    }

    @GetMapping("/example3")
    public ResponseEntity<?> example3() {
        Object body = new HashMap<>() {{
           put("key", "value");
        }};
        return new ResponseEntity<>(
                body,
                new MultiValueMapAdapter<>(Map.of("Job4jCustomHeader", List.of("job4j"))),
                HttpStatus.OK);
    }

    @GetMapping("/example4")
    public ResponseEntity<String> example4() {
        var body = new HashMap<>() {{
            put("key", "value");
        }}.toString();
        return ResponseEntity
                .status(HttpStatus.OK)
                .header("Job4jCustomHeader", "job4j")
                .contentType(MediaType.TEXT_PLAIN)
                .contentLength(body.length())
                .body(body);
    }

    @GetMapping("/example5")
    public ResponseEntity<byte[]> example5() throws IOException {
        var content = Files.readAllBytes(Path.of("C:/LRA_RU.pdf"));
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(content.length)
                .body(content);
    }

    @GetMapping("/example6")
    public byte[] example6() throws IOException {
        return Files.readAllBytes(Path.of("C:/LRA_RU.pdf"));
    }
}
