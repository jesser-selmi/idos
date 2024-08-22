package com.IDOSdigital.userManagement.config;

import graphql.language.StringValue;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import graphql.schema.GraphQLScalarType;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateScalar {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static GraphQLScalarType build() {
        return GraphQLScalarType.newScalar()
                .name("Date")
                .description("A custom scalar that handles Java LocalDate")
                .coercing(new Coercing<LocalDate, String>() {

                    @Override
                    public String serialize(Object dataFetcherResult) throws CoercingSerializeException {
                        if (dataFetcherResult instanceof LocalDate) {
                            return DATE_FORMATTER.format((LocalDate) dataFetcherResult);
                        }
                        throw new CoercingSerializeException("Invalid value '" + dataFetcherResult + "' for Date");
                    }

                    @Override
                    public LocalDate parseValue(Object input) throws CoercingParseValueException {
                        try {
                            if (input instanceof String) {
                                return LocalDate.parse((String) input, DATE_FORMATTER);
                            }
                            throw new CoercingParseValueException("Invalid value '" + input + "' for Date");
                        } catch (Exception e) {
                            throw new CoercingParseValueException("Invalid value '" + input + "' for Date");
                        }
                    }

                    @Override
                    public LocalDate parseLiteral(Object input) throws CoercingParseLiteralException {
                        if (input instanceof StringValue) {
                            return LocalDate.parse(((StringValue) input).getValue(), DATE_FORMATTER);
                        }
                        throw new CoercingParseLiteralException("Invalid literal '" + input + "' for Date");
                    }
                })
                .build();
    }
}
