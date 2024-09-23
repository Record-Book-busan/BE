package busim.kkilogbu.user.entity;

import busim.kkilogbu.user.entity.interest.Interest;
import busim.kkilogbu.user.entity.users.Users;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class UserInterest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "users_id", nullable = false)
    private Users users;

    @ManyToOne
    @JoinColumn(name = "interest_id")
    private Interest interest;

    public UserInterest(Users users, Interest interest) {
        this.users = users;
        this.interest = interest;
    }
}
