package de.johanneswirth.tac.authservice;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.jdbi.v3.core.config.ConfigRegistry;
import org.jdbi.v3.core.generic.GenericTypes;
import org.jdbi.v3.core.mapper.*;

import java.lang.reflect.Type;
import java.util.Optional;

public class PairMapperFactory implements RowMapperFactory {
    public Optional<RowMapper<?>> build(Type type, ConfigRegistry config) {
        if (!Pair.class.equals(GenericTypes.getErasedType(type))) {
            return Optional.empty();
        }

        Type leftType = GenericTypes.resolveType(Pair.class.getTypeParameters()[0], type);
        Type rightType = GenericTypes.resolveType(Pair.class.getTypeParameters()[1], type);

        ColumnMappers columnMappers = config.get(ColumnMappers.class);

        ColumnMapper<?> leftMapper = columnMappers.findFor(leftType)
                .orElseThrow(() -> new NoSuchMapperException(
                        "No column mapper registered for Pair left parameter " + leftType));
        ColumnMapper<?> rightMapper = columnMappers.findFor(rightType)
                .orElseThrow(() -> new NoSuchMapperException(
                        "No column mapper registered for Pair right parameter " + rightType));

        RowMapper<?> pairMapper = (rs, ctx) ->
                new ImmutablePair(leftMapper.map(rs, 1, ctx),
                        rightMapper.map(rs, 2, ctx));

        return Optional.of(pairMapper);
    }
}
