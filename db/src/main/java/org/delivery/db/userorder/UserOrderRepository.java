package org.delivery.db.userorder;

import org.delivery.db.userorder.enums.UserOrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserOrderRepository extends JpaRepository<UserOrderEntity, Long> {

    /**
     * SELECT * FROM user_order WHERE user_id = ? AND status = ? ORDER BY id DESC
     */
    List<UserOrderEntity> findAllByUserIdAndStatusOrderByIdDesc(Long userId, UserOrderStatus status);


    /**
     * SELECT * FROM user_order WHERE user_id = ? AND status IN (?,? .. ) ORDER BY id DESC
     */
    List<UserOrderEntity> findAllByUserIdAndStatusInOrderByIdDesc(Long userId, List<UserOrderStatus> status);

    /**
     * SELECT * FROM user_order WHERE id = ? AND status = ? AND user_id = ?
     */
    Optional<UserOrderEntity> findAllByIdAndStatusAndUserId(Long id, UserOrderStatus status, Long userId);

    Optional<UserOrderEntity> findAllByIdAndUserId(Long id, Long userId);
}
