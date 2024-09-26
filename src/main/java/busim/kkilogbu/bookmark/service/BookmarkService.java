package busim.kkilogbu.bookmark.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import busim.kkilogbu.bookmark.entity.Bookmark;
import busim.kkilogbu.bookmark.repository.BookmarkRepository;
import busim.kkilogbu.global.Ex.BaseException;
import busim.kkilogbu.place.repository.PlaceRepository;
import busim.kkilogbu.record.entity.Records;
import busim.kkilogbu.record.repository.RecordRepository;
import busim.kkilogbu.user.entity.users.Users;
import busim.kkilogbu.user.repository.UserRepository;
import busim.kkilogbu.user.service.UserService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookmarkService {
	private final RecordRepository recordRepository;
	private final PlaceRepository placeRepository;
	private final BookmarkRepository bookmarkRepository;
	private final UserRepository userRepository;
	private final UserService userService;

	@Transactional
	public void bookmark(Long id){
		Users users = userService.getCurrentUser();
		userRepository.save(users);
		Records records = recordRepository.findById(id).orElseThrow(
			() -> new BaseException("존제하지 않는 records", HttpStatus.BAD_REQUEST)
		);
		if(bookmarkRepository.existsByUsersAndRecords(users, records)){
			throw new BaseException("이미 북마크 중입니다", HttpStatus.BAD_REQUEST);
		}
		Bookmark bookmark = Bookmark.builder().build();
		bookmarkRepository.save(bookmark);
		bookmark.connect(users, records, null);

	}

	@Transactional
	public void unbookmark(Long id, String type){
		Users users = userService.getCurrentUser();
		userRepository.save(users);

		Records records = recordRepository.findById(id).orElseThrow(
			() -> new BaseException("존제하지 않는 records", HttpStatus.BAD_REQUEST)
		);
		Bookmark bookmark = bookmarkRepository.findByUsersAndRecords(users, records).orElseThrow(
			() -> new BaseException("북마크 중이 아닙니다", HttpStatus.BAD_REQUEST)
		);
		bookmark.disconnect();
		bookmarkRepository.delete(bookmark);
	}
}
