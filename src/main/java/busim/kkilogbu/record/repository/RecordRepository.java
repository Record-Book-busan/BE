package busim.kkilogbu.record.repository;

import java.util.List;
import java.util.Optional;

import busim.kkilogbu.record.entity.Records;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import busim.kkilogbu.user.entity.users.Users;

@Repository
public interface RecordRepository extends JpaRepository<Records, Long> {
	@Query("SELECT r FROM Records r JOIN FETCH r.addressInfo JOIN FETCH r.contents WHERE r.addressInfo.id IN :addressInfoIds")
	List<Records> findByAddressInfoIn(List<Long> addressInfoIds);
	@Query("SELECT r FROM Records r JOIN FETCH r.addressInfo JOIN FETCH r.contents WHERE r.id = :id")
	Optional<Records> findFetchById(Long id);
	Page<Records> findByUsers(Users users, Pageable pageable);
	Optional<Records> findByUsersAndId(Users users, Long id);
	Page<Records> findAll(Pageable pageable);
}
