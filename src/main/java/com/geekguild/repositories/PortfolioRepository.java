package com.geekguild.repositories;

import com.geekguild.models.Group;
import com.geekguild.models.Portfolio;
import com.geekguild.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {

    Portfolio findByUserId(Long id);

}
