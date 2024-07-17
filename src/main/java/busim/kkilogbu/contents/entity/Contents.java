package busim.kkilogbu.contents.entity;

import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;

import java.util.ArrayList;
import java.util.List;

import busim.kkilogbu.hashTag.entity.HashTag;
import busim.kkilogbu.like.entity.Like;
import busim.kkilogbu.map.entity.Map;
import busim.kkilogbu.record.entity.Record;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
public class Contents {
	@Id @GeneratedValue(strategy = IDENTITY)
	private Long id;
	@OneToMany(mappedBy = "contents")
	private List<HashTag> hashTag = new ArrayList<>();
	private String content;
	private String imageUrl;
	@OneToOne(mappedBy = "contents", fetch = LAZY)
	private Like likes;

	@OneToOne(fetch = LAZY)
	@JoinColumn(name = "record_id")
	private Record record;

	@OneToOne(fetch = LAZY)
	@JoinColumn(name = "map_id")
	private Map map;
}
