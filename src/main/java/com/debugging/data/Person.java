package com.debugging.data;

import io.micronaut.data.annotation.AutoPopulated;
import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.data.annotation.Relation;
import java.util.List;

@MappedEntity
public class Person {

    @Id
    @GeneratedValue
    @AutoPopulated
    Integer id;

    Integer age;

    @Relation(value = Relation.Kind.ONE_TO_MANY, mappedBy = "person", cascade = Relation.Cascade.ALL)
    List<Name> names;

    public Person() {
    }

    public Person(Integer age, List<Name> names) {
        this.age = age;
        this.names = names;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Name> getNames() {
        return names;
    }

    public void setNames(List<Name> names) {
        this.names = names;
    }
}
