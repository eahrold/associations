package com.debugging.data

import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject

@MicronautTest
class PersonRepositoryTest extends IntegrationTestSpecification {

    @Inject
    PersonRepository personRepository

    @Inject
    NameRepository nameRepository

    def "Test Injections"() {
        expect:
        personRepository != null
        nameRepository != null
    }

    def "Test Delete Cascading"() {
        when:
        def person  = new Person(21, [new Name("jack"), new Name("jill")])
        def savedPerson = personRepository.save(person).block()

        then:
        noExceptionThrown()
        savedPerson.names.size() == 2
        savedPerson.age == 21

        when:
        savedPerson.names.removeIf(name -> name.getName() == 'jack')
        def updatedPerson = personRepository.update(savedPerson).block()

        then:
        savedPerson.names.size() == 1 // Still good here
        updatedPerson.names.size() == 1 // Still good here

        when:
        def fetchedPerson = personRepository.findById(savedPerson.id).block()

        then:
        fetchedPerson.names.size() == 1 // This fails, indicating that it never actually propagated down.

    }
}
