package busim.kkilogbu.user.repository;

import busim.kkilogbu.user.entity.UserConsent;
import busim.kkilogbu.user.entity.users.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserConsentRepository extends JpaRepository<UserConsent, Long> {

    boolean existsByUsersAndTermsAgreedTrueAndPrivacyAgreedTrue(Users users);

}
