package busim.kkilogbu.bookmark.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import busim.kkilogbu.bookmark.repository.BookmarkRepository;
import busim.kkilogbu.place.repository.PlaceRepository;
import busim.kkilogbu.record.repository.RecordRepository;
import busim.kkilogbu.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookmarkService {
	private final RecordRepository recordRepository;
	private final PlaceRepository placeRepository;
	private final BookmarkRepository bookmarkRepository;
	private final UserRepository userRepository;

//	@Transactional
//	public void bookmark(Long id, String type){
//		// TODO : 로그인 구현후 변경
//		Users users = Users.builder().nickname("tester").build();
//		userRepository.save(users);
//
//		if(type == "record"){
//			Records records = recordRepository.findById(id).orElseThrow(
//				() -> new IllegalArgumentException("존제하지 않는 records")
//			);
//			if(bookmarkRepository.existsByUserAndRecords(users, records)){
//				// TODO : 예외처리 변경
//				throw new IllegalArgumentException("이미 북마크 중입니다");
//			}
//			Bookmark bookmark = Bookmark.builder().build();
//			bookmarkRepository.save(bookmark);
//			bookmark.connect(users, records, null);
//		}
//		else if(type == "place"){
//			Place place = placeRepository.findById(id).orElseThrow(
//				() -> new IllegalArgumentException("존제하지 않는 place")
//			);
//			if(bookmarkRepository.existsByUserAndPlace(users, place)){
//				// TODO : 예외처리 변경
//				throw new IllegalArgumentException("이미 북마크 중입니다");
//			}
//			Bookmark bookmark = Bookmark.builder().build();
//			bookmarkRepository.save(bookmark);
//			bookmark.connect(users, null, place);
//		}
//		else{
//			// TODO : 예외처리 변경
//			throw new IllegalArgumentException("type is not valid");
//		}
//	}
//
//	@Transactional
//	public void unbookmark(Long id, String type){
//		// TODO : 로그인 구현후 변경
//		Users users = Users.builder().nickname("tester").build();
//		userRepository.save(users);
//
//		Bookmark bookmark;
//		if(type == "record"){
//			Records records = recordRepository.findById(id).orElseThrow(
//				() -> new IllegalArgumentException("존제하지 않는 records")
//			);
//			bookmark = bookmarkRepository.findByUserAndRecords(users, records).orElseThrow(
//				// TODO : 예외처리 변경
//				() -> new IllegalArgumentException("북마크 중이 아닙니다")
//			);
//			bookmark.disconnect();
//		}
//		else if(type == "place"){
//			Place place = placeRepository.findById(id).orElseThrow(
//				() -> new IllegalArgumentException("존제하지 않는 place")
//			);
//			bookmark = bookmarkRepository.findByUserAndPlace(users, place).orElseThrow(
//				// TODO : 예외처리 변경
//				() -> new IllegalArgumentException("북마크 중이 아닙니다")
//			);
//			bookmark.disconnect();
//		}
//		else{
//			throw new IllegalArgumentException("type is not valid");
//		}
//		bookmarkRepository.delete(bookmark);
//	}
}
