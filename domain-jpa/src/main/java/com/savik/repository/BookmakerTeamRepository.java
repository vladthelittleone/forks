package com.savik.repository;

import com.savik.domain.BookmakerPK;
import com.savik.domain.BookmakerTeam;
import org.springframework.stereotype.Repository;

@Repository
public interface BookmakerTeamRepository extends JpaEntryRepository<BookmakerTeam, BookmakerPK> {

}