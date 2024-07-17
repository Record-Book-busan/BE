package busim.kkilogbu.test;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class TestClass {
	@Id @GeneratedValue
	private Long id;
}
