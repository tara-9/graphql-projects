package com.example.graphqljava.config;

import graphql.GraphQLContext;
import graphql.execution.CoercedVariables;
import graphql.language.StringValue;
import graphql.language.Value;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import graphql.schema.GraphQLScalarType;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Configuration
public class ScalarConfig {

    public static final GraphQLScalarType DateScalar = GraphQLScalarType.newScalar()
            .name("Date")
            .description("A custom scalar that handles Date")
            .coercing(new Coercing() {
                @Override
                public Object serialize(@NotNull Object dataFetcherResult, @NotNull GraphQLContext graphQLContext, @NotNull Locale locale) throws CoercingSerializeException {
                    try {
                        Instant publishedTime = (Instant) dataFetcherResult;
                        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE.withZone(ZoneId.systemDefault());
                        return formatter.format(publishedTime);
                    } catch (CoercingSerializeException exception) {
                        throw new CoercingSerializeException("Invalid Input:"+exception.getMessage());
                    }
                }

                @Override
                public Object parseValue(@NotNull Object input, @NotNull GraphQLContext graphQLContext, @NotNull Locale locale) throws CoercingParseValueException {
                    try{
                        return LocalDate.parse((String) input).atStartOfDay(ZoneId.systemDefault()).toInstant();
                    } catch (RuntimeException exception) {
                        throw new CoercingParseValueException("Invalid Input:"+exception.getMessage());
                    }
                }

                @Override
                public Object parseLiteral(@NotNull Value input, @NotNull CoercedVariables variables, @NotNull GraphQLContext graphQLContext, @NotNull Locale locale) throws CoercingParseLiteralException {
                    try {
                        StringValue stringValue = (StringValue) input;
                        LocalDate date = LocalDate.parse(stringValue.getValue());
                        return date.atStartOfDay(ZoneId.systemDefault()).toInstant();
                    } catch (RuntimeException exception) {
                        throw new CoercingParseLiteralException("Invalid Input:"+exception.getMessage());
                    }
                }

                @Override
                public @NotNull Value<?> valueToLiteral(@NotNull Object input, @NotNull GraphQLContext graphQLContext, @NotNull Locale locale) {
                    return new StringValue(input.toString());
                }
            })
            .build();
    @Bean
    public RuntimeWiringConfigurer runtimeWiringConfigurer() {
        return builder -> builder.scalar(DateScalar);
    }
}
