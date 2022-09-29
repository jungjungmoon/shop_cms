package shop.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shop.product.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

}
