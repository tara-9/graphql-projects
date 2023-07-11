### Union And Interface
##### Unions and interfaces are abstract GraphQL types that enable a schema field to return one of multiple object types

### Interface types
##### An interface specifies a set of fields that multiple object types can include:

#### How To define An interface
This is how we define an interface
```graphql
interface Product {
    id: ID!
    name: String!
    link: String!
}

```
This is how we can define object types that can implement that interface
```graphql
type Clothing implements Product {
    id: ID!
    name: String!
    link: String!
    size:String!

}
```
This is how It is mapped to the java class
```java
public interface Product {
}
```
```java
public class Clothing implements Product {

    private String id;

    private String name;

    private String link;

    private String size;
}
```
We can return this interface as field return type
```graphql
type Query {
    getProductDetails(id:String!): Product!
}

```
Here The return type is an interface. In run time it can be one of it's concrete object type.

For a sample java code to handle this scenario:[Interface Resolver Code](./src/main/java/com/example/graphqljava/service/ProductService.java)

For querying the fields which return an interface we can ask for all the fields present in the interface.
Also we can ask specific fields present in it's concrete object types using an inline fragment

```graphql
query{
  getProductDetails(id:"product-2"){
    name
    ... on Food{
      type
    }
    ... on Clothing {
      size
    }
  }
}
```
Here we are asking name field which is present in the interface and for fields that is specific to the concrete object types we are using an inline fragment to get.

Only Food has type property and only clothing has size property. So we can't ask in the top level fields but only within an inline fragment


### Union Types
##### It is a type which can combine multiple object types in the result.
A sample use case- Whenever User cart has products we return the products list otherwise if it is empty return a precomputed Recommendation list.

Here we are defining a union type which is a combination of list of product the cart has  and Recommendation

```graphql
union OrderCart = Food | Clothing | Recommendation
```
Then we can return this union as a query/mutation field return value.
```graphql
type Query {
    getCartResult: [OrderCart]!
}
```
We don't need any java entity class mapped to the union types.
For a sample implementation of the union return type pls see:[Union Type Return Code](./src/main/java/com/example/graphqljava/service/CartService.java)

For Querying the union return type fields we need to use inline fragment only for each object types even though they might have same fields.
```graphql
query{
  getCartResult{
    ... on Recommendation {
      name
      price
    }
    ... on Food {
      name
      type
    }
    ... on Clothing{
      name
      size
    }
    
  }
}
```
Although Here we have name field in every object types still we need to add it in every object types

### A [Medium Post](https://medium.com/@taraprasad9090/graphql-interface-and-union-541a5d5edb54) link if you like to read it there
