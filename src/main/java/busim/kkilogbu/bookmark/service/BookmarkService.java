package busim.kkilogbu.bookmark.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import busim.kkilogbu.bookmark.entity.Bookmark;
import busim.kkilogbu.bookmark.repository.BookmarkRepository;
import busim.kkilogbu.place.entity.Place;
import busim.kkilogbu.place.repository.PlaceRepository;
import busim.kkilogbu.record.entity.Record;
import busim.kkilogbu.record.repository.RecordRepository;
import busim.kkilogbu.user.entity.User;
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

	@Transactional
	public void bookmark(Long id, String type){
		// TODO : 로그인 구현후 변경
		User user = User.builder().nickname("tester").build();
		userRepository.save(user);

		if(type == "record"){
			Record record = recordRepository.findById(id).orElseThrow(
				() -> new IllegalArgumentException("존제하지 않는 record")
			);
			if(bookmarkRepository.existsByUserAndRecord(user, record)){
				// TODO : 예외처리 변경
				throw new IllegalArgumentException("이미 북마크 중입니다");
			}
			Bookmark bookmark = Bookmark.builder().build();
			bookmarkRepository.save(bookmark);
			bookmark.connect(user, record, null);
		}
		else if(type == "place"){
			Place place = placeRepository.findById(id).orElseThrow(
				() -> new IllegalArgumentException("존제하지 않는 place")
			);
			if(bookmarkRepository.existsByUserAndPlace(user, place)){
				// TODO : 예외처리 변경
				throw new IllegalArgumentException("이미 북마크 중입니다");
			}
			Bookmark bookmark = Bookmark.builder().build();
			bookmarkRepository.save(bookmark);
			bookmark.connect(user, null, place);
		}
		else{
			// TODO : 예외처리 변경
			throw new IllegalArgumentException("type is not valid");
		}
	}

	@Transactional
	public void unbookmark(Long id, String type){
		// TODO : 로그인 구현후 변경
		User user = User.builder().nickname("tester").build();
		userRepository.save(user);

		Bookmark bookmark;
		if(type == "record"){
			Record record = recordRepository.findById(id).orElseThrow(
				() -> new IllegalArgumentException("존제하지 않는 record")
			);
			bookmark = bookmarkRepository.findByUserAndRecord(user, record).orElseThrow(
				// TODO : 예외처리 변경
				() -> new IllegalArgumentException("북마크 중이 아닙니다")
			);
			bookmark.disconnect();
		}
		else if(type == "place"){
			Place place = placeRepository.findById(id).orElseThrow(
				() -> new IllegalArgumentException("존제하지 않는 place")
			);
			bookmark = bookmarkRepository.findByUserAndPlace(user, place).orElseThrow(
				// TODO : 예외처리 변경
				() -> new IllegalArgumentException("북마크 중이 아닙니다")
			);
			bookmark.disconnect();
		}
		else{
			throw new IllegalArgumentException("type is not valid");
		}
		bookmarkRepository.delete(bookmark);
	}
}
