package com.savik.specifications;

import com.savik.domain.Match;
import com.savik.domain.MatchStatus;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;

import static com.savik.specification.CommonSpecifications.equal;

@UtilityClass
public class MatchSpec {

    public static Specification<Match> isMatchStatus(MatchStatus matchStatus) {
        return equal("matchStatus", matchStatus);
    }

}
