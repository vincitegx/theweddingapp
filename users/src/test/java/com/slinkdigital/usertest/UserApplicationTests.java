package com.slinkdigital.usertest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import org.junit.jupiter.api.Test;

/**
 *
 * @author TEGA
 */
public class UserApplicationTests {
    
    Calculator underTest = new Calculator();
    
    @Test
    void itShouldAddTwoNumbers(){
        // given
        int a = 20;
        int b = 30;
        // when
        int result = underTest.add(a, b);
        // then
        int expected = 50;
        assertThat(result).isEqualTo(expected);
    }
    
    class Calculator{
        int add(int a, int b){return a + b;}
    }
}
