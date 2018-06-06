package com.savik.repository;

import com.savik.domain.Team;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends JpaEntryRepository<Team, String> {

    Team findByFlashscoreId(String code);

}
