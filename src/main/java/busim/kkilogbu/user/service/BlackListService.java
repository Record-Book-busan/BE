package busim.kkilogbu.user.service;

import busim.kkilogbu.global.Ex.BaseException;
import busim.kkilogbu.record.entity.Records;
import busim.kkilogbu.user.entity.BlackList;
import busim.kkilogbu.user.entity.users.Users;

import org.springframework.http.HttpStatus;
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
	private final UserService userService;
	@Transactional
	public void report(Long markId) {
		Users users = userService.getCurrentUser();
		Records records = recordRepository.findById(markId)
			.orElseThrow(() -> new BaseException("해당하는 기록이 없습니다.", HttpStatus.NOT_FOUND));
		Users writer = records.getUsers();
		if(isBlocked(users, writer)) {
			throw new BaseException("이미 신고한 사용자입니다.", HttpStatus.BAD_REQUEST);
		}
		BlackList blocked = BlackList.builder().users(users).reportedUsers(writer).build();
		blocked.report(users, writer);
		blackListRepository.save(blocked);
	}
	public boolean isBlocked(Users listener, Users speaker) {
		return blackListRepository.existsByUsersAndReportedUsers(listener, speaker);
	}
}
