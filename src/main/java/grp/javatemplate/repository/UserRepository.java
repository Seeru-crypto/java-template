package grp.javatemplate.repository;

import grp.javatemplate.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Page<User> findAll(Pageable pageable);

    boolean existsByName( String name );

//    @Query("""
//            SELECT CASE WHEN COUNT(f) > 0
//            THEN TRUE else FALSE end
//            FROM Flow f
//            WHERE f.product.id = :productId
//                                    """)
//    boolean existsByProductId(Long productId);
}