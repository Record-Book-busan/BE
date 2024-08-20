package busim.kkilogbu.record.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import busim.kkilogbu.record.entity.Record;
import busim.kkilogbu.user.entity.User;

@Repository
public interface RecordRepository extends JpaRepository<Record, Long> {
	@Query("SELECT r FROM Record r JOIN FETCH r.addressInfo JOIN FETCH r.contents WHERE r.addressInfo.id IN :addressInfoIds")
	List<Record> findByAddressInfoIn(List<Long> addressInfoIds);
	@Query("SELECT r FROM Record r JOIN FETCH r.addressInfo JOIN FETCH r.contents WHERE r.id = :id")
	Optional<Record> findFetchById(Long id);
	List<Record> findByUser(User user);
}
