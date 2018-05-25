package com.savik.repository;

import com.savik.domain.BookmakerLeague;
import com.savik.domain.BookmakerPK;
import org.springframework.stereotype.Repository;

@Repository
public interface BookmakerLeagueRepository extends JpaEntryRepository<BookmakerLeague, BookmakerPK> {

}