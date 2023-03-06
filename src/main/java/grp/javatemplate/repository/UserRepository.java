package grp.javatemplate.repository;

import grp.javatemplate.model.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findAll(Sort sort);

    boolean existsByName( String name );

//    @Query("""
//            SELECT CASE WHEN COUNT(f) > 0
//            THEN TRUE else FALSE end
//            FROM Flow f
//            WHERE f.product.id = :productId
//                                    """)
//    boolean existsByProductId(Long productId);
}