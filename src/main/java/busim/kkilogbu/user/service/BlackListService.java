package busim.kkilogbu.user.service;

import busim.kkilogbu.user.entity.users.Users;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import busim.kkilogbu.record.repository.RecordRepository;
import busim.kkilogbu.user.repository.BlackListRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BlackListService {
	private final BlackListRepository blackListRepository;
	private final RecordRepository recordRepository;
//	@Transactional
//	public void report(Long markId) {
//		// TODO : 로그인 구현후 수정
//		Users users = Users.builder().nickname("tester").build();
//		Records records = recordRepository.findById(markId)
//			.orElseThrow(() -> new BaseException("해당하는 기록이 없습니다.", HttpStatus.NOT_FOUND));
//		Users writer = records.getUsers();
//		BlackList blocked = BlackList.builder().users(users).reportedUsers(writer).build();
//		blocked.report(users, writer);
//		blackListRepository.save(blocked);
//	}
	public boolean isBlocked(Users listener, Users speaker) {
		return blackListRepository.existsByUsersAndReportedUsers(listener, speaker);
	}
}
