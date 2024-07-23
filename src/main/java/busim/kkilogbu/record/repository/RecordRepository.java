package busim.kkilogbu.record.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import busim.kkilogbu.record.entity.Record;

@Repository
public interface RecordRepository extends JpaRepository<Record, Long> {
	List<Record> findByAddressInfoIn(List<Long> addressInfoIds);
}
