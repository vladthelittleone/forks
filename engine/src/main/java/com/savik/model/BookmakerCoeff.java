package com.savik.model;

import com.savik.service.bookmaker.BookmakerCoeffMapper;
import com.savik.service.bookmaker.CoeffType;
import com.savik.utils.BookmakerUtils;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

import java.util.Arrays;
import java.util.List;

@Data
@Builder
public class BookmakerCoeff {

    Double typeValue;

    Double coeffValue;

    CoeffTypeChain typeChain;


    public static BookmakerCoeff of(Double typeValue, Double coeffValue, CoeffType... types) {
        return of(typeValue, coeffValue, Arrays.asList(types));
    }

    public static BookmakerCoeff of(Double typeValue, Double coeffValue, List<CoeffType> types) {
        return new BookmakerCoeff(typeValue, coeffValue, new CoeffTypeChain(types));
    }

    public BookmakerCoeff(Double typeValue, Double coeffValue, CoeffTypeChain typeChain) {
        this.typeValue = typeValue;
        this.coeffValue = coeffValue;
        this.typeChain = typeChain;
    }

    public boolean isForkCompatibleTypeInGeneral(BookmakerCoeff anotherCoeff) {
        CoeffTypeChain selfChain = getTypeChain();
        CoeffTypeChain anotherChain = anotherCoeff.getTypeChain();
        if (selfChain.size() != anotherChain.size()) {
            return false;
        }
        // first element is match, 1 half, 2 half, so should be equal
        return BookmakerCoeffMapper.isAcceptable(selfChain, anotherChain);
    }

    public boolean isForkCompatibleTypeInTypes(BookmakerCoeff anotherCoeff) {
        if (!isForkCompatibleTypeInGeneral(anotherCoeff)) {
            throw new IllegalArgumentException(String.format("it's not compatible fork type: self = %s, other = %s", toString(), anotherCoeff));
        }
        CoeffType selfLastChild = getLastChild();

        if (selfLastChild == CoeffType.HANDICAP) {
            return BookmakerUtils.isHandicapForkAcceptableTypes(getTypeValue(), anotherCoeff.getTypeValue());
        }
        if (selfLastChild == CoeffType.OVER) {
            return BookmakerUtils.isTotalForkAcceptableTypes(getTypeValue(), anotherCoeff.getTypeValue());
        }
        if (selfLastChild == CoeffType.UNDER) {
            return BookmakerUtils.isTotalForkAcceptableTypes(anotherCoeff.getTypeValue(), getTypeValue());
        }
        throw new IllegalArgumentException(String.format("Type handler doesn't exist: %s", getTypeChain()));
    }


    public boolean isFork(BookmakerCoeff anotherCoeff) {
        if (!isForkCompatibleTypeInTypes(anotherCoeff)) {
            throw new IllegalArgumentException(String.format("it's not compatible fork type: self = %s, other = %s", toString(), anotherCoeff));
        }
        CoeffType selfLastChildType = getLastChild();

        if (selfLastChildType == CoeffType.HANDICAP) {
            return BookmakerUtils.isFork(getCoeffValue(), anotherCoeff.getCoeffValue());
        }
        if (selfLastChildType == CoeffType.OVER) {
            return BookmakerUtils.isFork(getCoeffValue(), anotherCoeff.getCoeffValue());
        }
        if (selfLastChildType == CoeffType.UNDER) {
            return BookmakerUtils.isFork(getCoeffValue(), anotherCoeff.getCoeffValue());
        }
        throw new IllegalArgumentException("Type handler doesn't exist: %s" + getTypeChain());
    }


    public static double getForkPercentage(BookmakerCoeff first, BookmakerCoeff second) {
        return BookmakerUtils.getForkPercentage(first.getCoeffValue(), second.getCoeffValue());
    }

    private CoeffType getLastChild() {
        CoeffTypeChain typeChain = getTypeChain();
        return typeChain.get(typeChain.size() - 1);
    }

    @Override
    public String toString() {
        return "" +
                "typeValue=" + typeValue +
                ", coeffValue=" + coeffValue +
                ", typeChain=" + typeChain +
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
    @ToString
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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            CoeffTypeChain that = (CoeffTypeChain) o;

            return chain.equals(that.chain);
        }

        @Override
        public int hashCode() {
            // todo:
            return  5;
            //return chain.hashCode();
        }
    }
}
