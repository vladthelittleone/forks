package com.savik.repository;

import com.savik.domain.BookmakerTeam;
import com.savik.domain.BookmakerTeamPK;
import org.springframework.stereotype.Repository;

@Repository
public interface BookmakerTeamRepository extends JpaEntryRepository<BookmakerTeam, BookmakerTeamPK> {

}