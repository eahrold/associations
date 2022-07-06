package com.debugging.data;

import io.micronaut.data.annotation.AutoPopulated;
import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.data.annotation.Relation;
import java.util.Objects;

@MappedEntity
public class Name {

    @Id
    @AutoPopulated
    @GeneratedValue
    Integer id;

    @Relation(value = Relation.Kind.MANY_TO_ONE)
    private Person person;
    private String name;
    boolean alias;

    public Name() {
    }

    public Name(String name) {
        this.name = name;
        this.alias = false;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAlias() {
        return alias;
    }

    public void setAlias(boolean alias) {
        this.alias = alias;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Name name1 = (Name) o;
        return alias == name1.alias && Objects.equals(id, name1.id) && Objects.equals(person, name1.person) && Objects.equals(name, name1.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, person, name, alias);
    }
}
