package hello;

import static graphql.schema.idl.RuntimeWiring.newRuntimeWiring;

import java.util.Collections;

import graphql.schema.GraphQLSchema;
import graphql.schema.StaticDataFetcher;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import graphql.servlet.config.GraphQLConfiguration;
import graphql.servlet.GraphQLHttpServlet;
import javax.servlet.annotation.WebServlet;

@WebServlet(name = "HelloServlet", urlPatterns = {"graphql"}, loadOnStartup = 1)
public class HelloServlet extends GraphQLHttpServlet {

  @Override
  protected GraphQLConfiguration getConfiguration() {
    return GraphQLConfiguration.with(createSchema()).build();
  }

  private GraphQLSchema createSchema() {
        String schema =
                "type Query{"
                        + "hello: String "
                        + "customers: [Customer!]! "
                        + "}"
                        + "type Customer {id: ID!, firstName: String!, lastName: String!, yearOfBirth: Int!, email: String!}";

    SchemaParser schemaParser = new SchemaParser();
    TypeDefinitionRegistry typeDefinitionRegistry = schemaParser.parse(schema);

    RuntimeWiring runtimeWiring = newRuntimeWiring()
                .type("Query",
                        builder -> builder
                        .dataFetcher("hello", new StaticDataFetcher("world"))
                        .dataFetcher("customers", new StaticDataFetcher(Collections.nCopies(5000,
                                                new Customer("id", "firstName", "lastName", 1990,
                                                        "first.lastname@email.com")))))
                .build();

    SchemaGenerator schemaGenerator = new SchemaGenerator();
    return schemaGenerator.makeExecutableSchema(typeDefinitionRegistry, runtimeWiring);
  }

    public static class Customer {

        private final String id;
        private final String firstName;
        private final String lastName;
        private final int yearOfBirth;
        private final String email;

        public Customer(String id, String firstName, String lastName, int yearOfBirth,
                String email) {
            this.id = id;
            this.firstName = firstName;
            this.lastName = lastName;
            this.yearOfBirth = yearOfBirth;
            this.email = email;
        }

        public String getId() {
            return id;
        }
        public String getFirstName() {
            return firstName;
        }
        public String getLastName() {
            return lastName;
        }
        public int getYearOfBirth() {
            return yearOfBirth;
        }
        public String getEmail() {
            return email;
        }
    }

}
