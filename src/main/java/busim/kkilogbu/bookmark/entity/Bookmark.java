package busim.kkilogbu.bookmark.entity;

import static jakarta.persistence.FetchType.*;
import static lombok.AccessLevel.*;

import busim.kkilogbu.place.entity.Place;
import busim.kkilogbu.record.entity.Records;
import busim.kkilogbu.user.entity.users.Users;
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
	@JoinColumn(name = "users_id")
	private Users users;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "records_id")
	private Records records;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "place_id")
	private Place place;

	@Builder
	public Bookmark(Users users, Records records, Place place) {
		this.users = users;
		this.records = records;
		this.place = place;
	}

	public void connect(Users users, Records records, Place place) {
		this.users = users;
		users.getBookmarks().add(this);
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
		if(users != null) {
			users.getBookmarks().remove(this);
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
