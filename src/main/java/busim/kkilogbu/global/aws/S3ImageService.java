// package busim.kkilogbu.global.aws;
//
// import java.io.IOException;
// import java.util.Arrays;
// import java.util.List;
// import java.util.UUID;
//
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.core.io.UrlResource;
// import org.springframework.http.HttpHeaders;
// import org.springframework.http.ResponseEntity;
// import org.springframework.stereotype.Service;
// import org.springframework.web.multipart.MultipartFile;
//
// import com.amazonaws.AmazonServiceException;
// import com.amazonaws.services.s3.AmazonS3;
// import com.amazonaws.services.s3.model.AmazonS3Exception;
// import com.amazonaws.services.s3.model.ObjectMetadata;
//
// import lombok.RequiredArgsConstructor;
// import lombok.extern.slf4j.Slf4j;
//
// @Slf4j
// @Service
// @RequiredArgsConstructor
// public class S3ImageService {
// 	private final AmazonS3 amazonS3;
// 	@Value("${cloud.aws.s3.bucket}")
// 	private String bucket;
//
// 	/**
// 	 * 이미지 업로드
// 	 */
// 	// TODO : 확장저 제한하고 파일 null 여부 체크, 썸네일 사이즈로 중복 저장할ㅈ
// 	public String saveFile(MultipartFile multipartFile) throws IOException {
// 		if(multipartFile == null) {
// 			log.warn("파일이 존재하지 않습니다.");
// 			// TODO : 예외 처리
// 		}
// 		String originalFilename = multipartFile.getOriginalFilename();
// 		String extend = originalFilename.substring(originalFilename.lastIndexOf('.') + 1);
//
// 		// 허용되는 확장자 리스트
// 		List<String> allowedExtentionList = Arrays.asList("jpg", "jpeg", "png");
// 		if(!allowedExtentionList.contains(extend)) {
// 			log.warn("허용되지 않는 확장자입니다.");
// 			// TODO : 커스텀 예외 처리
// 			throw new AmazonS3Exception("허용되지 않는 확장자입니다.");
// 		}
// 		String fileName = createFileName(originalFilename).concat("." + extend);
// 		ObjectMetadata metadata = new ObjectMetadata();
// 		metadata.setContentLength(multipartFile.getSize());
// 		metadata.setContentType(multipartFile.getContentType());
//
// 		amazonS3.putObject(bucket, fileName, multipartFile.getInputStream(), metadata);
//		// TODO : 전체 URL말고 리소스만 보낼지 고민중
// 		String resourceURL = amazonS3.getUrl(bucket, fileName).toString();
// 		return resourceURL.substr(resourceURL.lastIndexOf("/")\);
// 	}
//
// 	public ResponseEntity<UrlResource> downloadImage(String originalFilename) {
// 		checkFilename(originalFilename);
//
// 		UrlResource urlResource = new UrlResource(amazonS3.getUrl(bucket, originalFilename));
// 		String contentDisposition = "attachment; filename=\"" +  originalFilename + "\"";
//
// 		// header에 CONTENT_DISPOSITION 설정을 통해 클릭 시 다운로드 진행
// 		return ResponseEntity.ok()
// 			.header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
// 			.body(urlResource);
// 	}
//
// 	// TODO : image 삭제
// 	public void deleteImage(String originalFilename) {
// 		try {
// 			checkFilename(originalFilename);
// 			amazonS3.deleteObject(bucket, originalFilename);
// 		} catch (AmazonS3Exception e) {
// 			log.error("Failed to delete " + originalFilename + ": " + e.getMessage());
// 			e.printStackTrace();
// 		} catch (AmazonServiceException e) {
// 			log.error("AWS error occurred: " + e.getMessage());
// 			e.printStackTrace();
// 		} catch (Exception e) {
// 			log.error("Unexpected error occurred: " + e.getMessage());
// 			e.printStackTrace();
// 		}
// 	}
//
// 	private void checkFilename(String originalFilename) {
// 		if(!amazonS3.doesObjectExist(bucket, originalFilename)) {
// 			log.warn("해당 파일이 존재하지 않습니다.");
// 			// TODO : 예외 처리
// 			throw new AmazonS3Exception("해당 파일이 존재하지 않습니다.");
// 		}
// 	}
//
// 	private String createFileName(String originalFilename) {
// 		return UUID.randomUUID().toString();
// 	}
// }
