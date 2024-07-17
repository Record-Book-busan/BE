package busim.kkilogbu.like.entity;

import static jakarta.persistence.FetchType.*;

import busim.kkilogbu.contents.entity.Contents;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "likes")
public class Like {
	@Id @GeneratedValue
	private Long id;
	private int count;
	@OneToOne(fetch = LAZY)
	@JoinColumn(name = "contents_id")
	private Contents contents;
}
