package com.savik.service.bookmaker;

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

    public boolean isFork(BookmakerCoeff anotherCoeff) {
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
        BookmakerCoeff selfLastChild = getLastChild();
        BookmakerCoeff anotherLastChild = anotherCoeff.getLastChild();

        if (selfLastChild.getType() == CoeffType.HANDICAP) {
            if (BookmakerUtils.isForkAcceptableTypes(selfLastChild.getTypeValue(), anotherLastChild.getTypeValue())) {
                return BookmakerUtils.isFork(selfLastChild.getCoeffValue(), anotherLastChild.getCoeffValue());
            }
        }
        return true;
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
        String typeStr =  "";
        while (temp.getChild() != null) {
            typeStr += temp.getType().toString() +  ".";
            temp = temp.getChild();
        }
        typeStr += temp.getType().toString();
        return "{" +
                "type=" + typeStr +
                ", typeValue=" + temp.getTypeValue() +
                ", coeff=" + temp.getCoeffValue() +
                '}';
    }
}
