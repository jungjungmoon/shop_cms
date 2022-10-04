package shop.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shop.order.entity.ProductOrder;

import java.util.Collection;

@Repository
public interface OrderRepository extends JpaRepository<ProductOrder, Long> {

    /**
     * Order status
     */

    long countByProductIdAndUserIdAndStatusIn(long productId, String userId, Collection<String> status);

}
