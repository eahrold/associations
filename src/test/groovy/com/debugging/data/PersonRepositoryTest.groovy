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

        def jack = savedPerson.names[0]
        def jill = savedPerson.names[1]

        then:
        noExceptionThrown()
        savedPerson.names.size() == 2
        savedPerson.age == 21
        jill.alias == false

        when:
        jill.alias = true
        jill.name = "jillian"
        savedPerson.names.removeIf(name -> name == jack)

        def updatedPerson = personRepository.update(savedPerson).block()

        then:
        savedPerson.names.size() == 1 // Still good here
        updatedPerson.names.size() == 1 // Still good here
        updatedPerson.names[0].name == 'jillian' //
        updatedPerson.names[0].alias == true

        when:
        def fetchedPerson = personRepository.findById(savedPerson.id).block()

        then:
        fetchedPerson.names.size() == 1 // This fails, indicating that it never actually propagated down.
        nameRepository.count().block() == 1 // This also fails, indicating that it never actually propagated down.
    }

    static class CascadeAssociationAction {
        List<Name> keep = new ArrayList<>()
        List<Name> discard = new ArrayList<>()

        CascadeAssociationAction() {}
    }

    def "Test Delete Cascading The Hard Way"() {
        when:
        def person  = new Person(21, [new Name("jack"), new Name("jill")])
        def savedPerson = personRepository.save(person).block()

        def jack = savedPerson.names[0]
        def jill = savedPerson.names[1]

        then:
        noExceptionThrown()
        savedPerson.names.size() == 2
        savedPerson.age == 21
        jill.alias == false

        when: "We do removing of names the hard way."
        jill.alias = true
        jill.name = "jillian"


        def action = new CascadeAssociationAction()

        // Add The Predicate
        savedPerson.names.each {
            if(it.name === 'jack') action.discard.add(it)
            else action.keep.add(it)
        }

        nameRepository.deleteAll(action.discard).block()
        savedPerson.setNames(action.keep)

        def updatedPerson = personRepository.update(savedPerson).block()

        then: "The active persistence context is correct"
        savedPerson.names.size() == 1 // Still good here
        updatedPerson.names.size() == 1 // Still good here

        when: "We go back and fetch the record"
        def fetchedPerson = personRepository.findById(savedPerson.id).block()

        then: "The updated value propagated of the remaining names worked"
        fetchedPerson.names.size() == 1 // This works but requires me to re-fetch the entity after save
        fetchedPerson.names[0].name == 'jillian' //
        fetchedPerson.names[0].alias == true

        and: "The Name repository count is correct"
        nameRepository.count().block() == 1 // Again we're ok now.
    }
}
