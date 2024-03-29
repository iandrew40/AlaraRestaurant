package alararestaurant.repository;

import alararestaurant.domain.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    Category findByName(String name);

    @Query(value = "SELECT c FROM Category c join Item i ON c.id = i.category " +
            "GROUP BY c.name, c.id ORDER BY SIZE(c.items) desc")
    List<Category> findByItemsByCount();

}
