package shop.manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shop.manager.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

}
