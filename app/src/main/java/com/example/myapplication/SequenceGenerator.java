package com.example.myapplication;
import java.util.Random;

public class SequenceGenerator {

    public static int getUserUniqueId()
    {
        Random random=new Random(8);
        return random.nextInt();
    }
}
