package busim.kkilogbu.bookmark.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import busim.kkilogbu.bookmark.entity.Bookmark;
import busim.kkilogbu.place.entity.Place;
import busim.kkilogbu.record.entity.Record;
import busim.kkilogbu.user.entity.User;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
	boolean existsByUserAndRecord(User user, Record record);
	boolean existsByUserAndPlace(User user, Place place);
	Optional<Bookmark> findByUserAndRecord(User user, Record record);
	Optional<Bookmark> findByUserAndPlace(User user, Place place);
	Slice<Bookmark> findByUser(User user, Pageable pageable);
}
