package busim.kkilogbu.contents.entity;

import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import org.springframework.util.StringUtils;

import busim.kkilogbu.place.entity.Place;
import busim.kkilogbu.record.entity.Records;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table
@NoArgsConstructor(access = PROTECTED)
@Getter
public class Contents {

	@Id @GeneratedValue(strategy = IDENTITY)
	private Long id;
	private String title;
	private String content;
	private String imageUrl;

	@OneToOne(fetch = LAZY)
	@JoinColumn(name = "record_id")
	private Records records;

	@OneToOne(fetch = LAZY)
	@JoinColumn(name = "place_id")
	private Place place;

	@Builder
	public Contents(String title, String content, String imageUrl) {
		this.title = title;
		this.content = content;
		this.imageUrl = imageUrl;
	}
	public void connect(Records records, Place place) {
		this.records = records;
		this.place = place;
	}

	public void update(String content, String title, String imageUrl) {
		if(StringUtils.hasText(content)) this.content = content;
		if(StringUtils.hasText(title)) this.title = title;
		if(StringUtils.hasText(imageUrl)) this.imageUrl = imageUrl;
	}
}
