package busim.kkilogbu.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import busim.kkilogbu.user.entity.BlackList;

@Repository
public interface BlackListRepository extends JpaRepository<BlackList, Long> {
}
