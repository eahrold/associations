package com.debugging.data;

import io.micronaut.data.annotation.Join;
import io.micronaut.data.repository.reactive.ReactorCrudRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.r2dbc.annotation.R2dbcRepository;
import javax.validation.constraints.NotNull;
import reactor.core.publisher.Mono;

@R2dbcRepository(dialect = Dialect.POSTGRES)
public interface PersonRepository extends ReactorCrudRepository<Person, Integer>{
    @Override
    @Join(value = "names", type = Join.Type.LEFT_FETCH)
    Mono<Person> findById(@NotNull Integer integer);
}
