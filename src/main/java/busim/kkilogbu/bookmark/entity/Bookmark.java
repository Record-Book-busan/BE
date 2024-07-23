package busim.kkilogbu.bookmark.entity;

import static jakarta.persistence.FetchType.*;

import busim.kkilogbu.place.entity.Place;
import busim.kkilogbu.record.entity.Record;
import busim.kkilogbu.user.entity.User;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;

@Entity
@Table
@Builder
@Getter
public class Bookmark {
	@Id @GeneratedValue
	private Long id;
	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "record_id")
	private Record record;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "contents_id")
	private Place map;

}
