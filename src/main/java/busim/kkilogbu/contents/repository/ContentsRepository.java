package busim.kkilogbu.contents.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import busim.kkilogbu.contents.entity.Contents;

@Repository
public interface ContentsRepository extends JpaRepository<Contents, Long> {
}
