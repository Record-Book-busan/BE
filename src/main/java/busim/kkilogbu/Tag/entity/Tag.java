package busim.kkilogbu.Tag.entity;

import java.util.ArrayList;
import java.util.List;

import busim.kkilogbu.hashTag.entity.HashTag;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table
public class Tag {
	@Id @GeneratedValue
	private Long id;
	private String tagName;
	@OneToMany(mappedBy = "tag")
	private List<HashTag> hashTag = new ArrayList<>();
}
