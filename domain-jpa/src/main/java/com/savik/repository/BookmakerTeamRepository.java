package com.savik.repository;

import com.savik.domain.BookmakerTeam;
import com.savik.domain.BookmakerPK;
import org.springframework.stereotype.Repository;

@Repository
public interface BookmakerTeamRepository extends JpaEntryRepository<BookmakerTeam, BookmakerPK> {

}