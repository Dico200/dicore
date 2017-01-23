package io.dico.dicore.util.generator;

import junit.framework.TestCase;

import java.util.HashSet;
import java.util.Set;

import static io.dico.dicore.util.generator.PreparingGenerator.doYield;

public class GeneratorTest extends TestCase {
    
    public void testGenerator() {
        
        Set<String> set = new HashSet<>();
        for (String string : AwesomeGenerator.<String>generator(() -> {
            System.out.println("hi");
            doYield("x");
            System.out.println("hi");
            doYield("y");
            System.out.println("hi");
            doYield("z");
            System.out.println("hi");
        })) {
            set.add(string);
        }
        
        assertTrue(set.contains("x"));
        assertTrue(set.contains("y"));
        assertTrue(set.contains("z"));
    }
    
}
