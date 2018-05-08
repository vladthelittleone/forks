package com.savik.domain;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@MappedSuperclass
public abstract class Identifiable {

    @Id
    @SequenceGenerator(name="sequence_id", sequenceName="sequence_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_id")
    private Long id;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Identifiable that = (Identifiable) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
