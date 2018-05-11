package com.savik.repository;

import com.savik.Match;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FutureMatchRepository extends JpaEntryRepository<Match, String> {

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
