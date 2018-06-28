package com.savik.model;

import com.savik.service.bookmaker.BookmakerCoeffMapper;
import com.savik.service.bookmaker.CoeffType;
import com.savik.utils.BookmakerUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class BookmakerCoeff {

    CoeffType type;

    Double typeValue;

    Double coeffValue;

    BookmakerCoeff child;

    public static BookmakerCoeff of(Double typeValue, Double coeffValue, CoeffType... types) {
        return of(typeValue, coeffValue, Arrays.asList(types));
    }

    public static BookmakerCoeff of(Double typeValue, Double coeffValue, List<CoeffType> types) {
        BookmakerCoeff current = new BookmakerCoeff(types.get(0), typeValue, coeffValue);
        for (int i = 1; i < types.size(); i++) {
            CoeffType coeffType = types.get(i);
            current = new BookmakerCoeff(coeffType, current);
        }
        return current;
    }

    public BookmakerCoeff(CoeffType type, BookmakerCoeff child) {
        this.type = type;
        this.child = child;
    }

    public BookmakerCoeff(CoeffType type, Double typeValue, Double coeffValue) {
        this.type = type;
        this.typeValue = typeValue;
        this.coeffValue = coeffValue;
    }

    public boolean isForkCompatibleTypeInGeneral(BookmakerCoeff anotherCoeff) {
        List<BookmakerCoeff> selfChain = getChain();
        List<BookmakerCoeff> anotherChain = anotherCoeff.getChain();
        if (selfChain.size() != anotherChain.size()) {
            return false;
        }
        // first element is match, 1 half, 2 half, so should be equal
        if (!selfChain.get(0).getType().equals(anotherChain.get(0).getType())) {
            return false;
        }
        for (int i = 1; i < selfChain.size(); i++) {
            BookmakerCoeff origin = selfChain.get(i);
            BookmakerCoeff target = anotherChain.get(i);
            if (!BookmakerCoeffMapper.isAcceptable(origin.getType(), target.getType())) {
                return false;
            }
        }
        return true;
    }

    public boolean isForkCompatibleTypeInTypes(BookmakerCoeff anotherCoeff) {
        if (!isForkCompatibleTypeInGeneral(anotherCoeff)) {
            throw new IllegalArgumentException(String.format("it's not compatible fork type: self = %s, other = %s", toString(), anotherCoeff));
        }
        BookmakerCoeff selfLastChild = getLastChild();
        BookmakerCoeff anotherLastChild = anotherCoeff.getLastChild();

        if (selfLastChild.getType() == CoeffType.HANDICAP) {
            return BookmakerUtils.isHandicapForkAcceptableTypes(selfLastChild.getTypeValue(), anotherLastChild.getTypeValue());
        }
        if (selfLastChild.getType() == CoeffType.OVER) {
            return BookmakerUtils.isTotalForkAcceptableTypes(selfLastChild.getTypeValue(), anotherLastChild.getTypeValue());
        }
        if (selfLastChild.getType() == CoeffType.UNDER) {
            return BookmakerUtils.isTotalForkAcceptableTypes(anotherLastChild.getTypeValue(), selfLastChild.getTypeValue());
        }
        throw new IllegalArgumentException(String.format("Type handler doesn't exist: %s", selfLastChild.getType()));
    }


    public boolean isFork(BookmakerCoeff anotherCoeff) {
        if (!isForkCompatibleTypeInTypes(anotherCoeff)) {
            throw new IllegalArgumentException(String.format("it's not compatible fork type: self = %s, other = %s", toString(), anotherCoeff));
        }
        BookmakerCoeff selfLastChild = getLastChild();
        BookmakerCoeff anotherLastChild = anotherCoeff.getLastChild();

        if (selfLastChild.getType() == CoeffType.HANDICAP) {
            return BookmakerUtils.isFork(selfLastChild.getCoeffValue(), anotherLastChild.getCoeffValue());
        }
        if (selfLastChild.getType() == CoeffType.OVER) {
            return BookmakerUtils.isFork(selfLastChild.getCoeffValue(), anotherLastChild.getCoeffValue());
        }
        if (selfLastChild.getType() == CoeffType.UNDER) {
            return BookmakerUtils.isFork(selfLastChild.getCoeffValue(), anotherLastChild.getCoeffValue());
        }
        throw new IllegalArgumentException("Type handler doesn't exist: %s" + selfLastChild.getType());
    }


    public static double getForkPercentage(BookmakerCoeff first, BookmakerCoeff second) {
        BookmakerCoeff selfLastChild = first.getLastChild();
        BookmakerCoeff anotherLastChild = second.getLastChild();
        return BookmakerUtils.getForkPercentage(selfLastChild.getCoeffValue(), anotherLastChild.getCoeffValue());
    }

    private List<BookmakerCoeff> getChain() {
        List<BookmakerCoeff> chain = new ArrayList<>();
        BookmakerCoeff temp = this;
        while (temp != null) {
            chain.add(temp);
            temp = temp.getChild();
        }
        return chain;
    }

    private BookmakerCoeff getLastChild() {
        List<BookmakerCoeff> chain = getChain();
        return chain.get(chain.size() - 1);
    }

    @Override
    public String toString() {
        BookmakerCoeff temp = this;
        String typeStr = "";
        while (temp.getChild() != null) {
            typeStr += temp.getType().toString() + ".";
            temp = temp.getChild();
        }
        typeStr += temp.getType().toString();
        return "{" +
                "type=" + typeStr +
                ", typeValue=" + temp.getTypeValue() +
                ", coeff=" + temp.getCoeffValue() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BookmakerCoeff that = (BookmakerCoeff) o;

        if (type != that.type) return false;
        if (typeValue != null ? !typeValue.equals(that.typeValue) : that.typeValue != null) return false;
        if (coeffValue != null ? !coeffValue.equals(that.coeffValue) : that.coeffValue != null) return false;
        return child != null ? child.equals(that.child) : that.child == null;
    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + (typeValue != null ? typeValue.hashCode() : 0);
        result = 31 * result + (coeffValue != null ? coeffValue.hashCode() : 0);
        result = 31 * result + (child != null ? child.hashCode() : 0);
        return result;
    }
}
