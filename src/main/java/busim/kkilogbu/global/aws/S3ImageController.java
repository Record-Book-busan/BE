// package busim.kkilogbu.global.aws;
//
// import java.io.IOException;
//
// import org.springframework.core.io.UrlResource;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.DeleteMapping;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestPart;
// import org.springframework.web.bind.annotation.RestController;
// import org.springframework.web.multipart.MultipartFile;
//
// import lombok.RequiredArgsConstructor;
//
// @RestController
// @RequiredArgsConstructor
// @RequestMapping("/api/image")
// public class S3ImageController {
// 	private final S3ImageService s3ImageService;
//
// 	/**
// 	 * 이미지 다운로드
// 	 */
// 	@GetMapping("/{imageName}")
// 	public ResponseEntity<UrlResource> downloadImage(@PathVariable("imageName") String name) {
// 		return s3ImageService.downloadImage(name);
// 	}
//
// 	/**
// 	 * 이미지 업로드
// 	 */
// 	@PostMapping
// 	public ResponseEntity<String> uploadImage(@RequestPart MultipartFile image) throws IOException {
// 		return ResponseEntity.ok().body(s3ImageService.saveFile(image));
// 	}
//
// 	/**
// 	 * 이미지 삭제
// 	 */
// 	@DeleteMapping("/{imageName}")
// 	public void deleteImage(@PathVariable String imageName) {
// 		s3ImageService.deleteImage(imageName);
// 	}
// }
