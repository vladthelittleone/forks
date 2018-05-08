package com.savik;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Team {

    @Id
    private String flashcoreId;

    private String name;

}
