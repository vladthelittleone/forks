package com.savik.flashscore.rest;

import com.savik.domain.Match;
import com.savik.domain.MatchStatus;
import com.savik.filter.Filter;
import lombok.Value;
import org.springframework.data.jpa.domain.Specification;

import static com.savik.specifications.MatchSpec.isMatchStatus;

@Value
public class MatchFilter implements Filter<Match> {

    private MatchStatus matchStatus;

    @Override
    public Specification<Match> toSpecification() {
        return Specification.where(
                isMatchStatus(matchStatus)
        );
    }
}
