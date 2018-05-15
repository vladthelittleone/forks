package com.savik.repository;

import com.savik.domain.Match;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MatchRepository extends JpaEntryRepository<Match, String> {

    Match findByFlashscoreId(String code);

    @Override
    default List<Match> findAll() {
        Sort defaultSort = new Sort(Sort.Direction.ASC, "date");
        return findAll(defaultSort);
    }

    @Modifying
    @Transactional
    void deleteByDateBefore(LocalDateTime expiryDate);

}
