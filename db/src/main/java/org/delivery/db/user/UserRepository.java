package org.delivery.db.user;

import org.delivery.db.user.enums.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    /**
     * SELECT * FROM user WHERE id = ? AND status = ? ORDER BY id DESC LIMIT 1
     */
    Optional<UserEntity> findFirstByIdAndStatusOrderByIdDesc(Long userId, UserStatus status);

    /**
     * SELECT * FROM user WHERE email = ? AND password = ? AND status = ? ORDER BY id DESC LIMIT 1
     */
    Optional<UserEntity> findFirstByEmailAndPasswordAndStatusOrderByIdDesc(String email, String password, UserStatus status);
}
