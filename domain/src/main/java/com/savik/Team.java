package com.savik;

import com.savik.domain.Identifiable;

import javax.persistence.Entity;

@Entity
public class Team extends Identifiable {

    private String name;

    private String flashcoreId;


}
