### GraphQL Custom Scalar

The leaf nodes of the GraphQL type system are called scalars. Once you reach a scalar type, you cannot descend any further into the type hierarchy. A scalar type is meant to represent an indivisible value.

GraphQL provides certain scalar types like:String,Int,Float etc by default but usually developers need custom scalars. We will discuss in this article how we can create those

### Declare Custom Scalars In Schema

First we need to declare custom scalars in our schema file.  
We will use a date scalar for this example which pattern is `yyyy-mm-dd`. Below line shows how we can define a scalar.

`scalar Date` 

Then we can use this in any field. In this example we will add  a new field called publishedTime for the book object and define it's type as Date

```graphql
type Book {
    id: ID!
    name: String!
    pageCount: Int!
    price: Int!
    author: Author!
    publishedDate:Date!
}
```

### How To Serialize And DeSerialize The Scalars

Now we need to define how graphql engine can serialize and deserialize this scalar.

We will create a config file **ScalarConfig** which will hold the configuration for the scalars and define a GraphQLScalarType object.

``` Java
public static final GraphQLScalarType DateScalar = GraphQLScalarType.newScalar()
.name("Date")
.description("A custom scalar that handles Date")
```
Spring uses the value provided in the name method to know this object is responsible for which scalar type. In our case it is `Date`

```java
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
```
This code block tells how to serialize the scalar type. In our java code the publishedTime field is an **Instant** type but the server expects a Date Scalar. So we convert an instant type to a date scalar using this method.

The result for query for a bookDetails looks like this.
```json
{
  "data": {
    "bookById": {
      "publishedDate": "2020-01-23"
    }
  }
}
```
In the Book object the publishedDate looks like this:`2020-01-23T15:04:01Z`

Similarly we have a new mutation api called `updateBookPublishedDate` which can update the published date of the book. It takes a **date** as it's second argument which type is `Date`. We need to convert this argument to Instant

```java
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
```
These two methods will help us achieve that.  
- parseValue - takes a variable input object and converts into the Java runtime representation.  
- parseLiteral - takes an AST literal graphql.language.Value as input and converts into the Java runtime representation.

To understand more let's look at an example:
```graphql
mutation updateBooK($date:Date!) {
    updateBookPublishedDate(id:"book-1", date:$date){
    price
    publishedDate
    }
}
```
Here the date value is a variable input object i.e it will be passed under graphql variables object as this:
```json
{
  "date": "2021-09-10"
}
```
So in this case parseValue will be called

```graphql
mutation{
 updateBookPublishedDate(id:"book-1", date:"2021-06-12"){
  price
  publishedDate
}
}
```

Here we are inlining  the value of date field. In this case parseLiteral will be called.

### How To Register this scalar to spring auto-configuration

Now we need to wire this scalar to spring configuration so that it can register and use the scalar.

```java
@Bean
public RuntimeWiringConfigurer runtimeWiringConfigurer() {
    return builder -> builder.scalar(DateScalar);
}
```