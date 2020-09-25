package de.codeshield.cloudscan.serverlessGoatJava;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import org.junit.jupiter.api.Test;

public class AppTest {

  @Test
  public void handleRequest_shouldReturnConstantValue() throws IOException {

    String table =
        " | com.sun.activation:jakarta.activation:jar:1.2.2                   | ---           | org.glassfish.jaxb:jaxb-runtime:jar:2.3.3                         | ---                |\n"
            + "| com.sun.activation:javax.activation:jar:1.2.0                     | ---           | com.sun.activation:javax.activation:jar:1.2.0                     | ---                |\n"
            + "| com.sun.istack:istack-commons-runtime:jar:3.0.11                  | ---           | com.sun.istack:istack-commons-runtime:jar:3.0.11                  | ---                |\n"
            + "| jakarta.activation:jakarta.activation-api:jar:1.2.2               | ---           | jakarta.xml.bind:jakarta.xml.bind-api:jar:2.3.3                   | ---                |\n"
            + "| jakarta.xml.bind:jakarta.xml.bind-api:jar:2.3.3                   | ---           | jakarta.xml.bind:jakarta.xml.bind-api:jar:2.3.3                   | ---                |\n"
            + "| javax.annotation:javax.annotation-api:jar:1.3.2                   | ---           | javax.annotation:javax.annotation-api:jar:1.3.2                   | ---                |\n"
            + "| junit:junit:jar:4.12                                              | ---           | junit:junit:jar:4.12                                              | ---                |\n"
            + "| log4j:log4j:jar:1.2.17                                            | ---           | org.slf4j:slf4j-log4j12:jar:1.7.30                                | ---                |\n"
            + "| org.glassfish.jaxb:jaxb-runtime:jar:2.3.3                         | ---           | org.glassfish.jaxb:jaxb-runtime:jar:2.3.3                         | ---                |\n"
            + "| org.glassfish.jaxb:txw2:jar:2.3.3                                 | ---           | org.glassfish.jaxb:txw2:jar:2.3.3                                 | ---                |\n"
            + "| org.glassfish.main.external:jmxremote_optional-repackaged:jar:5.0 | ---           | org.glassfish.main.external:jmxremote_optional-repackaged:jar:5.0 | ---                |\n"
            + "| org.hamcrest:hamcrest-core:jar:1.3                                | ---           | junit:junit:jar:4.12                                              | ---                |\n"
            + "| org.javapos:javapos-config-loader:jar:2.3.1                       | ---           | org.javapos:javapos-config-loader:jar:2.3.1                       | ---                |\n"
            + "| org.javapos:javapos-contracts:jar:1.14.3                          | ---           | org.javapos:javapos-contracts:jar:1.14.3                          | ---                |\n"
            + "| org.javapos:javapos-controls:jar:1.14.1                           | ---           | org.javapos:javapos-controls:jar:1.14.1                           | ---                |\n"
            + "| org.slf4j:slf4j-api:jar:1.7.30                                    | ---           | org.slf4j:slf4j-api:jar:1.7.30                                    | ---                |\n"
            + "| org.slf4j:slf4j-log4j12:jar:1.7.30                                | ---           | org.slf4j:slf4j-log4j12:jar:1.7.30                                | ---                |\n"
            + "| xerces:xerces:jar:1.2.3                                           | ---           | org.javapos:javapos-config-loader:jar:2.3.1                       | ---                |\n"
            + "| xerces:xercesImpl:jar:2.12.0                                      | ---           | xerces:xercesImpl:jar:2.12.0                                      | ---                |\n"
            + "| xml-apis:xml-apis:jar:1.0.b2                                      | ---           | xml-apis:xml-apis:jar:1.0.b2                                      | ---                |\n"
            + "| xml-resolver:xml-resolver:jar:1.2                                 | ---           | xml-resolver:xml-resolver:jar:1.2                                 | ---                |";

    String[] split1 = table.replaceAll("\\|\n","").split("\\|");
    for (int i = 3; i < split1.length; i+=4) {

        String s = split1[i];

        String[] split = s.split(":");

        System.out.println(
            String.format(
                "<dependency>\n"
                    + "    <groupId>%s</groupId>\n"
                    + "    <artifactId>%s</artifactId>\n"
                    + "    <version>%s</version>\n"
                    + "</dependency>\n",
                split[0], split[1], split[3]));


    }

    App app = new App();
    Object result = app.handleRequest(null, null);
    assertEquals("echo", result);
  }
}
