package shortproject1;
import utils.Timer;
//import java.util.*;

public class TestClass {
    public static void main (String[] args) {
        Timer t = new Timer();
        t.start();
        for(int j=0; j < 200; j++ ) {
            System.out.println(j);
        }
        t.end();
        System.out.println(t);
    }
}
