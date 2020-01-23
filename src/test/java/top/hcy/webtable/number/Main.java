package top.hcy.webtable.number;


import org.junit.jupiter.api.Test;

import java.util.UUID;

public class Main {


    @Test
    public void intTo64S(){
        long l = System.currentTimeMillis();
        String s = Long.toString(l, 36);
        System.out.println(s);
    }
}
