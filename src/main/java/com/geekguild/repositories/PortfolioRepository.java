package com.geekguild.repositories;

//import com.geekguild.models.Group;
import com.geekguild.models.Portfolio;
import com.geekguild.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {

    @Query("SELECT p FROM Portfolio p WHERE p.user.id = :userId")
    Portfolio findByUserId(Long userId);

}
