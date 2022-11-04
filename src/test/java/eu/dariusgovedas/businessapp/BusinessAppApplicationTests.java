package eu.dariusgovedas.businessapp;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource("/application-test.properties")
class BusinessAppApplicationTests {

    @Test
    void contextLoads() {
    }

}
