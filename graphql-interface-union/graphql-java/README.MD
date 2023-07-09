### Union And Interface
##### Unions and interfaces are abstract GraphQL types that enable a schema field to return one of multiple object types

### Union Types

### How Union And Interface Works in SpringBoot Graphql
##### First we need to declare those in schema file.

```graphql
interface Product {
    id: ID!
    name: String!
    link: String!
}

```
```graphql
union OrderCart = Clothing | Food
```
##### For type Interface We need to declare concrete types which will have all the fields from the interface and any extra field it needs to declare
```graphql
type Clothing implements Product {
    id: ID!
    name: String!
    link: String!
    size:String!

}
```