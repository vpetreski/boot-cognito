package io.vanja.cognito;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class CognitoApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(CognitoApplication.class)
                .profiles("private")
                .run(args);
    }
}
