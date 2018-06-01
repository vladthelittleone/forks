package com.savik.service.bookmaker;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class BookmakerCoeff {

    CoeffType type;

    Double typeValue;

    Double coeffValue;

    BookmakerCoeff child;

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
        BookmakerCoeff lastChild = getLastChild();
        return "{" +
                "type=" + getChain().stream().map(BookmakerCoeff::getType).map(t -> toString()).reduce("", (s1, s2) -> s1 + "." + s2) +
                ", typeValue=" + lastChild.getTypeValue() +
                ", coeff=" + lastChild.getCoeffValue() +
                '}';
    }
}
