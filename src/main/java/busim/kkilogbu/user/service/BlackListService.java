package busim.kkilogbu.user.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import busim.kkilogbu.global.Ex.BaseException;
import busim.kkilogbu.record.entity.Records;
import busim.kkilogbu.record.repository.RecordRepository;
import busim.kkilogbu.user.entity.BlackList;
import busim.kkilogbu.user.entity.users.User;
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
//		User user = User.builder().nickname("tester").build();
//		Records records = recordRepository.findById(markId)
//			.orElseThrow(() -> new BaseException("해당하는 기록이 없습니다.", HttpStatus.NOT_FOUND));
//		User writer = records.getUser();
//		BlackList blocked = BlackList.builder().user(user).reportedUser(writer).build();
//		blocked.report(user, writer);
//		blackListRepository.save(blocked);
//	}
	public boolean isBlocked(User listener, User speaker) {
		return blackListRepository.existsByUserAndReportedUser(listener, speaker);
	}
}
