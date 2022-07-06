package com.debugging.data

import io.micronaut.test.support.TestPropertyProvider
import spock.lang.Specification

import javax.transaction.Transactional

@Transactional
class IntegrationTestSpecification extends Specification implements DatabaseFixture, TestPropertyProvider {
    void setup() {}

    void cleanup() {}

    @Override
    Map<String, String> getProperties() {
        return [:] + getDatabaseProperties()
    }
}
