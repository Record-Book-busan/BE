package busim.kkilogbu.global.aws;

import java.io.IOException;

import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/image")
public class S3ImageController {
	private final S3ImageService s3ImageService;

	/**
	 * 이미지 다운로드
	 */
	@GetMapping
	public ResponseEntity<UrlResource> downloadImage(@PathParam("imageName") String name) {
		return s3ImageService.downloadImage(name);
	}

	/**
	 * 이미지 업로드
	 */
	@PostMapping
	public ResponseEntity<String> uploadImage(@RequestPart MultipartFile image, @RequestParam("type") String type) throws IOException {
		return ResponseEntity.ok().body(s3ImageService.saveFile(image, type));
	}

	/**
	 * 이미지 삭제
	 */
	@DeleteMapping
	public void deleteImage(@PathParam("name") String name) {
		s3ImageService.deleteImage(name);
	}
}
