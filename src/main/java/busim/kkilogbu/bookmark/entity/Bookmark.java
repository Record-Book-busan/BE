package busim.kkilogbu.bookmark.entity;

import static jakarta.persistence.FetchType.*;
import static lombok.AccessLevel.*;

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
import lombok.NoArgsConstructor;

@Entity
@Table
@NoArgsConstructor(access = PROTECTED)
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
	@JoinColumn(name = "place_id")
	private Place place;

	@Builder
	public Bookmark(User user, Record record, Place place) {
		this.user = user;
		this.record = record;
		this.place = place;
	}

	public void connect(User user, Record record, Place place) {
		this.user = user;
		user.getBookmarks().add(this);
		if(record != null) {
			this.record = record;
			record.getBookmark().add(this);
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
		if(record != null) {
			record.getBookmark().remove(this);
		}
		if(place != null) {
			place.getBookmark().remove(this);
		}
	}
	public boolean isRecord() {
		return record != null;
	}
}
