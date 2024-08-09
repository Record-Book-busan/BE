package busim.kkilogbu.bookmark.repository;

import java.util.Optional;

import busim.kkilogbu.user.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import busim.kkilogbu.bookmark.entity.Bookmark;
import busim.kkilogbu.place.entity.Place;
import busim.kkilogbu.record.entity.Record;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
	boolean existsByUserAndRecord(Users users, Record record);
	boolean existsByUserAndPlace(Users users, Place place);
	Optional<Bookmark> findByUserAndRecord(Users users, Record record);
	Optional<Bookmark> findByUserAndPlace(Users users, Place place);
}
