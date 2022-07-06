package com.debugging.data

import io.micronaut.data.model.query.builder.sql.Dialect
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.spock.Testcontainers
import org.testcontainers.utility.DockerImageName
import spock.lang.Shared

@Testcontainers
trait DatabaseFixture {

    static String getPostgresImageName() {
        return "postgres:12"
    }

    @Shared
    private static final PostgreSQLContainer DB_CONTAINER = new PostgreSQLContainer(
            DockerImageName.parse(getPostgresImageName())
    )

    Map<String, String> getDatabaseProperties() {
        if (!DB_CONTAINER.isRunning()) DB_CONTAINER.start()

        var props = [
                "r2dbc.datasources.default.url"     : "r2dbc:postgresql://${DB_CONTAINER.getHost()}:${DB_CONTAINER.getFirstMappedPort()}/test",
                "r2dbc.datasources.default.username": DB_CONTAINER.getUsername(),
                "r2dbc.datasources.default.password": DB_CONTAINER.getPassword(),
                "r2dbc.datasources.default.dialect" : Dialect.POSTGRES.toString()
        ]

        return props
    }

}