package io.project.fastwork.repositories;

import io.project.fastwork.domains.StatusUser;
import io.project.fastwork.domains.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Users,Long> {
    @Query("select u from Users u where u.username=:username")
    Users findByLogin(@Param("username")String username);
    @Query("select u from Users u where u.id=:user_id")
    Users getUserById(@Param("user_id")Long user_id);
    @Query("select u from Users u where u.userEmail=:email")
    Users findbyEmail(@Param("email")String email);

}
