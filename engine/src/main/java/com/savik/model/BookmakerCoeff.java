package com.savik.model;

import com.savik.service.bookmaker.BookmakerCoeffMapper;
import com.savik.service.bookmaker.CoeffType;
import lombok.Builder;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Builder
@Getter
public class BookmakerCoeff {

    String typeValue;

    Double coeffValue;

    CoeffTypeChain typeChain;

    boolean lay = false;

    public static BookmakerCoeff of(Double typeValue, Double coeffValue, CoeffType... types) {
        return of(typeValue, coeffValue, Arrays.asList(types));
    }

    public static BookmakerCoeff of(String typeValue, Double coeffValue, CoeffType... types) {
        return new BookmakerCoeff(typeValue, coeffValue, new CoeffTypeChain(types));
    }

    public static BookmakerCoeff of(Double coeffValue, CoeffType... types) {
        return of(null, coeffValue, Arrays.asList(types));
    }

    public static BookmakerCoeff of(Double typeValue, Double coeffValue, List<CoeffType> types) {
        return new BookmakerCoeff(String.valueOf(typeValue), coeffValue, new CoeffTypeChain(types));
    }

    public BookmakerCoeff lay() {
        this.lay = true;
        return this;
    }

    public BookmakerCoeff(String typeValue, Double coeffValue, CoeffTypeChain typeChain) {
        this.typeValue = typeValue;
        this.coeffValue = coeffValue;
        this.typeChain = typeChain;
    }

    public BookmakerCoeff(String typeValue, Double coeffValue, CoeffTypeChain typeChain, boolean lay) {
        this.typeValue = typeValue;
        this.coeffValue = coeffValue;
        this.typeChain = typeChain;
        this.lay = lay;
    }

    public Double getTypeValueAsDouble() {
        return Double.valueOf(typeValue);
    }

    public boolean isBetCompatibleByMeaning(BookmakerCoeff anotherCoeff) {
        return BookmakerCoeffMapper.isAcceptable(this, anotherCoeff);
    }

    public boolean isBetCompatibleByValue(BookmakerCoeff anotherCoeff) {
        if (!isBetCompatibleByMeaning(anotherCoeff)) {
            throw new IllegalArgumentException(String.format("it's not compatible bets by chains: self = %s, other = %s", toString(), anotherCoeff));
        }
        return BookmakerCoeffMapper.isBetCompatibleByValue(this, anotherCoeff);
    }

    public boolean isFork(BookmakerCoeff anotherCoeff) {
        if (!isBetCompatibleByValue(anotherCoeff)) {
            throw new IllegalArgumentException(String.format("it's not compatible bets by value self = %s, other = %s", toString(), anotherCoeff));
        }
        return BookmakerCoeffMapper.isFork(this, anotherCoeff);
    }

    public boolean isSame(BookmakerCoeff coeff) {
        return typeChain.equals(coeff.getTypeChain()) && typeValue.equals(coeff.getTypeValue()) && coeff.lay == lay;
    }

    public boolean isBackLaySame(BookmakerCoeff coeff) {
        return typeChain.equals(coeff.getTypeChain()) && Objects.equals(typeValue, coeff.getTypeValue()) && coeff.lay != lay;
    }

    @Override
    public String toString() {
        return "" +
                "tV= " + typeValue +
                ", cV= " + coeffValue +
                ", lay= " + lay +
                ", ch= " + typeChain +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BookmakerCoeff that = (BookmakerCoeff) o;

        if (typeValue != null ? !typeValue.equals(that.typeValue) : that.typeValue != null) return false;
        if (!coeffValue.equals(that.coeffValue)) return false;
        return typeChain.equals(that.typeChain);
    }

    @Override
    public int hashCode() {
        int result = typeValue != null ? typeValue.hashCode() : 0;
        result = 31 * result + coeffValue.hashCode();
        result = 31 * result + typeChain.hashCode();
        return result;
    }

    @Getter
    public static class CoeffTypeChain {

        List<CoeffType> chain;

        public CoeffTypeChain(List<CoeffType> chain) {
            this.chain = chain;
        }

        public CoeffTypeChain(CoeffType... types) {
            this(Arrays.asList(types));
        }

        public CoeffType get(int index) {
            return chain.get(index);
        }

        public int size() {
            return chain.size();
        }

        public CoeffType getLastChild() {
            return chain.get(chain.size() - 1);
        }

        @Override
        public String toString() {
            return chain.toString();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            CoeffTypeChain that = (CoeffTypeChain) o;

            return chain.equals(that.chain);
        }

        @Override
        public int hashCode() {
            return chain.stream().map(Enum::toString).mapToInt(s -> s.hashCode()).sum();
        }
    }
}
