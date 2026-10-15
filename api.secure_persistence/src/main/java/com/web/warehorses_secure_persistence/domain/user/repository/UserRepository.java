package com.web.warehorses_secure_persistence.domain.user.repository;

import com.web.warehorses_secure_persistence.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByIdCardNumber(String idCardNumber);

    /**
     * Non-throwing alternative to findByIdCardNumber: that method raises
     * UserNotFoundException when the row is absent, which makes it unusable
     * for a 'does this already exist?' check during registration.
     */
    boolean existsByIdCardNumber(String idCardNumber);
}
