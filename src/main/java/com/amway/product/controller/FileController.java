package com.amway.product.controller;

import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import com.amway.product.service.StorageService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
@Slf4j
public class FileController {
	
    private final StorageService service;
	

	
	@PostMapping(value = "/upload", produces = MediaType.TEXT_PLAIN_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public Mono<String> uploadFile(@RequestPart("files") Mono<FilePart> filePartFlux, @RequestParam("productCode") String productCode) {
		return filePartFlux.flatMap(file -> {
			Path fileContent = Paths.get(file.filename());
			return file.transferTo(fileContent).then(service.uploadFile(fileContent, productCode));


		}).doOnError(error-> {
			log.error("Exception : ", error);
			error.printStackTrace();
		}).onErrorResume(error -> {
			log.error("Exception : ", error);
			return Mono.just("Error uploading files");
		});
	}
	
	@PostMapping(value = "/upload1", produces = MediaType.TEXT_PLAIN_VALUE)
	public Mono<String> uploadFile1(@RequestBody Flux<ByteBuffer> body, @RequestParam("productCode") String productCode,
			@RequestHeader HttpHeaders headers) {

		return service.saveFile(body, headers, productCode);
	}
	
	



    @GetMapping("/download/{fileName}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable String fileName) {
        byte[] data = service.downloadFile(fileName);
        ByteArrayResource resource = new ByteArrayResource(data);
        return ResponseEntity
                .ok()
                .contentLength(data.length)
                .header("Content-type", "application/octet-stream")
                .header("Content-disposition", "attachment; filename=\"" + fileName + "\"")
                .body(resource);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteFile(@RequestParam String fileName) {
        return new ResponseEntity<>(service.deleteFile(fileName), HttpStatus.OK);
    }
}
