package com.savik.service.bookmaker;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

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

    @Override
    public String toString() {
        BookmakerCoeff temp = child;
        String typeStr =  type.toString();
        while (temp.getChild() != null) {
            typeStr += "." + temp.getType().toString();
            temp = temp.getChild();
        }
        typeStr += "." + temp.getType().toString();
        return "{" +
                "type=" + typeStr +
                ", typeValue=" + temp.getTypeValue() +
                ", coeff=" + temp.getCoeffValue() +
                '}';
    }
}
