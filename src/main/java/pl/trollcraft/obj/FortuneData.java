package pl.trollcraft.obj;

import java.util.Random;

public class FortuneData {

    private static final Random RAND = new Random();

    private int minimum;
    private int maximum;

    public FortuneData(int minimum, int maximum){
        this.minimum = minimum;
        this.maximum = maximum;
    }

    public int getAmount() { return RAND.nextInt(maximum - minimum + 1) + minimum; }

}
