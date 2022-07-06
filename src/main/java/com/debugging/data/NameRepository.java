package com.debugging.data;

import io.micronaut.data.repository.reactive.ReactorCrudRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.r2dbc.annotation.R2dbcRepository;

@R2dbcRepository(dialect = Dialect.POSTGRES)
public interface NameRepository extends ReactorCrudRepository<Name, Integer> {
}
