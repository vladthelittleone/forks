package com.savik;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(SpringExtension.class)
@IntegrationTest
public class ListenerIntegrationTest {

    @Test
    public void test() {
        assertEquals(1, 1);
    }
}