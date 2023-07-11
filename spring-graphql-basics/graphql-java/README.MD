### GraphQL Basics
#### What is Graphql?
From the official doc: GraphQL is a query language for your API, and a server-side runtime for executing queries using a type system you define for your data.

It defines a schema for the data and then client can use the schema to understand how the data is organized and ask a part of the data which it needs.

#### GraphQL in Java/SpringBoot EcoSystem
GraphQL is a specification for how data can be fetched. Each Framework can provide their own runtime engines to support the specification. 

[GraphQL-Java](https://github.com/graphql-java/graphql-java) is the official graphql engine for java and [spring for graphql](https://github.com/spring-projects/spring-graphql) build on top of it providing autoconfiguration and other useful functionalities out of the box.
#### GraphQL-SpringBoot Deep Dive
##### Graphql Schema File
GraphQL requires a schema file to know about the structure of the data. A schema file consists of types and fields. There are some predefined types and we can create custom types also. 

A example of the schema file is:
```graphql
type Query {
    bookById(id: ID): Book!
}

type Mutation {
    updateBookPrice(id:ID!, price:Int!) : Book!
}

type Book {
    id: ID!
    name: String!
    pageCount: Int!
    price: Int!
    author: Author!
}

type Author {
    id: ID!
    firstName: String!
    lastName: String
}
```
Here Book And Author are user defined types.  
ID and String and Int are some predefined types.  
Query is a special type as it enables graphql engine to use the fields defined under query for getting data.  
Mutation is also a special type and it is used to change the state of the data.  
The “!” marks against the field specifies that their value can’t be null ever.  
Spring looks for this schema file under src/main/resources/graphql/** folder with file extension as .graphqls or .gqls.

#### GraphQL Schema Mapping To Java Code
Now we have the schema we should have concrete implementation for those types and fields. 

Each user defined types should be present as pojo class and each query and mutation should be present as a function with return type same as in schema file and getting same argument(**although it can take additional argument which are automatically passed by graphql engine**).

```java
public class Book {
    private String id;
    private String name;
    private int pageCount;
    private String authorId;
    private int price;

    public Book(String id, String name, int pageCount, String authorId, int price) {
        this.id = id;
        this.name = name;
        this.pageCount = pageCount;
        this.authorId = authorId;
        this.price = price;
    }
}
```
This is the code for mapping the user defined types to  pojo class.  
The second part is mapping our query and mutation operation. Here SpringBoot provides annotation support for mapping different types.
```java
@Controller
public class BookController {

    @Autowired
    BookService bookService;

    @Autowired
    HttpServletRequest httpServletRequest;

    @QueryMapping
    public Book bookById(@Argument String id, DataFetchingEnvironment env, GraphQLContext context) {
        return bookService.findBookById(id);
    }

    @MutationMapping
    public Book updateBookPrice(@Argument String id, @Argument int price) {
        String currency = httpServletRequest.getHeader("currency");
        if(currency.equalsIgnoreCase("euro")) return bookService.updatePriceOfBook(id, price * 2);
        return bookService.updatePriceOfBook(id, price);
    }

    @SchemaMapping
    public Author author(Book book) {
        return bookService.findAuthorById(book.getAuthorId());
    }
}
```
Query mapping is for mapping query to function. It is a nothing but schema mapping with type as Query.

Similarly Mutation mapping is for mapping mutation objects to function. It is also a specialized schema mapping with type as Mutation.  

Schema mapping is used for resolving fields which can’t be automatically resolved by graphql. It is called datafetcher in graphql language.  

As graphql engine starts to resolve the book object it needs the author object to resolve. As book only has the author id we need to declare how that can be resolved. Graphql engine automatically pass the Book object that till now has been resolved and using the author id we resolve the author field and eventually the call completes with the book object containing author object. Schema mapping annotation helps in informing graphql engine how to resolve those field which it can’t itself resolve.

### GraphQL Request Headers
We can pass request header for a query and mutation operation. To access those headers we can autowire HttpServletRequest and use getHeader method of it.

```java
@Autowired
HttpServletRequest httpServletRequest;

@MutationMapping
public Book updateBookPrice(@Argument String id, @Argument int price) {
    String currency = httpServletRequest.getHeader("currency");
    if(currency.equalsIgnoreCase("euro")) return bookService.updatePriceOfBook(id, price * 2);
    return bookService.updatePriceOfBook(id, price);
}
```

In the above example first we are getting the currency from header and depending on it’s value we are updating the price.

### Where To Execute GraphQL Operation
Spring Graphql has a code editor where we can try the query and mutation operation. The editor is graphiql. It is disbaled by default. We can enable it by setting this property `spring.graphql.graphiql.enabled=true`. Then we need to go into `localhost:8080/graphiql` and use respective operation.
```graphql
query{
  bookById(id:"book-1") {
    id
    price
    name
  }
}
```
This is a sample query operation.
```graphql
mutation{
  updateBookPrice(id:"book-1", price:50) {
    id
    price
  }
}
```
This is a sample mutation operation.

#### Here is the [medium post](https://medium.com/@taraprasad9090/a-beginner-guide-to-spring-with-graphql-95aad336616d) for the same if u like to read there