package com.example.demo;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
public class SpringTestExample {

    @Test
    public void test() {
        assertEquals(2,2);
    }
}
