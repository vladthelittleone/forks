package com.savik.specification;

import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import javax.persistence.metamodel.PluralAttribute;
import javax.persistence.metamodel.SingularAttribute;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@UtilityClass
public class CommonSpecifications {

    public static <T, V> Specification<T> equal(String attr, V value) {
        if (value == null) {
            return null;
        } else if (value instanceof String) {
            if (StringUtils.isEmpty(value)) {
                return null;
            }
        }
        return (root, query, cb) -> cb.equal(root.get(attr), value);
    }

    public static <T, V> Specification<T> equal(SingularAttribute attr, V value) {
        if (value == null) {
            return null;
        } else if (value instanceof String) {
            if (StringUtils.isEmpty(value)) {
                return null;
            }
        }
        return (root, query, cb) -> cb.equal(root.get(attr), value);
    }

    public static <T, V> Specification<T> anyEquals(V value, SingularAttribute ... attributes) {
        return (root, query, cb) -> {
            Function<SingularAttribute, Predicate> mapper = singularAttribute -> cb.equal(root.get(singularAttribute), value);
            List<Predicate> predicates = Stream.of(attributes)
                    .map(mapper)
                    .collect(Collectors.toList());
            return cb.or(
                    predicates.toArray(new Predicate[predicates.size()])
            );
        };
    }

    public static <T> Specification<T> contain(String attr, String value) {
        if (StringUtils.isEmpty(value)) {
            return null;
        }
        return (root, query, cb) -> cb.like(cb.lower(root.get(attr)), "%" + value.toLowerCase() + "%");
    }

    public static <T> Specification<T> contain(SingularAttribute attr, String value) {
        if (StringUtils.isEmpty(value)) {
            return null;
        }
        return (root, query, cb) -> cb.like(cb.lower(root.get(attr)), "%" + value.toLowerCase() + "%");
    }

    public static <T> Specification<T> startsWith(String attr, String value) {
        if (StringUtils.isEmpty(value)) {
            return null;
        }
        return (root, query, cb) -> cb.like(cb.lower(root.get(attr)), value.toLowerCase() + "%");
    }

    public static <T> Specification<T> start(String attr, String value) {
        if (StringUtils.isEmpty(value)) {
            return null;
        }
        return (root, query, cb) -> cb.like(cb.lower(root.get(attr)), value.toLowerCase() + "%");
    }

    public static <T> Specification<T> end(String attr, String value) {
        if (StringUtils.isEmpty(value)) {
            return null;
        }
        return (root, query, cb) -> cb.like(cb.lower(root.get(attr)), "%" + value.toLowerCase());
    }

    public static <T, V> Specification<T> in(String attr, Collection<V> value) {
        if (CollectionUtils.isEmpty(value)) {
            return null;
        }
        return (root, query, cb) -> root.get(attr).in(value);
    }

    public static <T, V> Specification<T> notIn(String attr, List<V> value) {
        if (CollectionUtils.isEmpty(value)) {
            return null;
        }
        return (root, query, cb) -> root.get(attr).in(value).not();
    }

    public static <T, V> Specification<T> isNull(String attr) {
        return (root, query, cb) -> cb.isNull(root.get(attr));
    }

    public static <T, V> Specification<T> isNotNull(String attr) {
        return (root, query, cb) -> cb.isNotNull(root.get(attr));
    }

    public static <T> Specification<T> isTrue(String attr) {
        return (root, query, cb) -> cb.isTrue(root.get(attr));
    }

    public static <T> Specification<T> isFalse(String attr) {
        return (root, query, cb) -> cb.isFalse(root.get(attr));
    }

    public static <T> Specification<T> is(String attr, Boolean value) {
        if (value == null) {
            return null;
        }
        return (root, query, cb) -> value ? cb.isTrue(root.get(attr)) : cb.isFalse(root.get(attr));
    }

    public static <T> Specification<T> is(String attr, String value) {
        if (value != null) {
            if ("true".equalsIgnoreCase(value)) {
                return is(attr, Boolean.TRUE);
            }
            if ("false".equalsIgnoreCase(value)) {
                return is(attr, Boolean.FALSE);
            }
        }
        return null;
    }

    public static <T, E> Specification<T> greaterThan(String attr, Integer value) {
        return (root, query, cb) -> cb.greaterThan(cb.size(root.get(attr)), value);
    }

    public static <T> Specification<T> dateBetween(
            String attr, LocalDateTime from, LocalDateTime to
    ) {
        if (from == null || to == null) {
            return null;
        }
        return (root, query, cb) -> cb.between(root.get(attr), from, to);
    }

    public static <T> Specification<T> before(String attr, LocalDateTime threshold) {
        if (threshold == null) {
            return null;
        }
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.<LocalDateTime>get(attr), threshold);
    }

    public static <T> Specification<T> after(String attr, LocalDateTime threshold) {
        if (threshold == null) {
            return null;
        }
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.<LocalDateTime>get(attr), threshold);
    }

    public static <T extends Serializable, E> Specification<T> isMember(
            PluralAttribute<T, List<E>, E> attr,
            E value
    ) {
        return (root, query, cb) -> cb.isMember(value, root.get(attr));
    }

    public static <T> Specification<T> like(String attr, String value) {
        return (root, query, cb) -> like(value).map(val -> cb.like(cb.lower(root.get(attr)), val)).orElse(null);
    }

    private static Optional<String> like(String query) {
        if (query == null) {
            return Optional.empty();
        }
        query = query.trim();
        if (query.isEmpty()) {
            return Optional.empty();
        }
        query = new StringBuilder(2 + query.length()).append("%").append(query).append("%").toString();
        return Optional.of(query.toLowerCase());
    }
}
