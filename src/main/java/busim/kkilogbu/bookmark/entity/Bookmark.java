package busim.kkilogbu.bookmark.entity;

import static jakarta.persistence.FetchType.*;
import static lombok.AccessLevel.*;

import busim.kkilogbu.place.entity.Place;
import busim.kkilogbu.record.entity.Records;
import busim.kkilogbu.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table
@NoArgsConstructor(access = PROTECTED)
@Getter
public class Bookmark {


	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;


	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "records_id")
	private Records records;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "place_id")
	private Place place;

	@Builder
	public Bookmark(User user, Records records, Place place) {
		this.user = user;
		this.records = records;
		this.place = place;
	}

	public void connect(User user, Records records, Place place) {
		this.user = user;
		user.getBookmarks().add(this);
		if(records != null) {
			this.records = records;
			records.getBookmark().add(this);
		}
		if(place != null) {
			this.place = place;
			place.getBookmark().add(this);
		}
	}

	public void disconnect() {
		if(user != null) {
			user.getBookmarks().remove(this);
		}
		if(records != null) {
			records.getBookmark().remove(this);
		}
		if(place != null) {
			place.getBookmark().remove(this);
		}
	}
	public boolean isRecord() {
		return records != null;
	}
}
