package busim.kkilogbu.bookmark.repository;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import busim.kkilogbu.bookmark.entity.Bookmark;
import busim.kkilogbu.place.entity.Place;
import busim.kkilogbu.record.entity.Records;
import busim.kkilogbu.user.entity.users.Users;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
	boolean existsByUsersAndRecords(Users users, Records records);
	boolean existsByUsersAndPlace(Users users, Place place);
	Optional<Bookmark> findByUsersAndRecords(Users users, Records records);
	Optional<Bookmark> findByUsersAndPlace(Users users, Place place);
	Slice<Bookmark> findByUsers(Users users, Pageable pageable);
	Slice<Bookmark> findByUsersAndRecordIsNotNull(Users users, Pageable pageable);
	Slice<Bookmark> findByUsersAndPlaceIsNotNull(Users users, Pageable pageable);
}
