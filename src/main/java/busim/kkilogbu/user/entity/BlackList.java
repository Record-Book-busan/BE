package busim.kkilogbu.user.entity;

import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import busim.kkilogbu.user.entity.users.Users;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Builder
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class BlackList {
	@Id @GeneratedValue(strategy = IDENTITY)
	private Long id;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "users_id")
	private Users users;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "reported_user_id")
	private Users reportedUsers;

	public void report(Users users, Users reportedUsers) {
		users.getBlackLists().add(this);
	}
}
