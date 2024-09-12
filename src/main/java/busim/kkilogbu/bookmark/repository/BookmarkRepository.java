package busim.kkilogbu.bookmark.repository;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import busim.kkilogbu.bookmark.entity.Bookmark;
import busim.kkilogbu.place.entity.Place;
import busim.kkilogbu.record.entity.Records;
import busim.kkilogbu.user.entity.users.User;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
	boolean existsByUserAndRecords(User user, Records records);
	boolean existsByUserAndPlace(User user, Place place);
	Optional<Bookmark> findByUserAndRecords(User user, Records records);
	Optional<Bookmark> findByUserAndPlace(User user, Place place);
	Slice<Bookmark> findByUser(User user, Pageable pageable);
	Slice<Bookmark> findByUserAndRecordIsNotNull(User user, Pageable pageable);
	Slice<Bookmark> findByUserAndPlaceIsNotNull(User user, Pageable pageable);
}
