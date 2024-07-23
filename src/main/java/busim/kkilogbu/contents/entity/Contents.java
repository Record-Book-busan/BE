package busim.kkilogbu.contents.entity;

import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;

import busim.kkilogbu.place.entity.Place;
import busim.kkilogbu.record.entity.Record;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;

@Entity
@Table
@Builder
@Getter
public class Contents {
	@Id @GeneratedValue(strategy = IDENTITY)
	private Long id;
	private String title;
	private String content;
	private String imageUrl;
	@OneToOne(fetch = LAZY)
	@JoinColumn(name = "record_id")
	private Record record;

	@OneToOne(fetch = LAZY)
	@JoinColumn(name = "map_id")
	private Place map;


}
