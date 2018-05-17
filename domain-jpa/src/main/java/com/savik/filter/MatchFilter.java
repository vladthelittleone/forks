package com.savik.filter;

import com.savik.domain.Match;
import com.savik.domain.MatchStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import static com.savik.specifications.MatchSpec.isMatchStatus;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class MatchFilter implements Filter<Match> {

    private MatchStatus matchStatus;

    @Override
    public Specification<Match> toSpecification() {
        return Specification.where(
                isMatchStatus(matchStatus)
        );
    }
}
