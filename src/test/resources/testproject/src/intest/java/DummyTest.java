import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DummyTest {

    @Test
    public void test() {
        Assertions.assertEquals(new Dummy().one(), 1);
        System.out.println("DummyTest.test SUCCESS");
    }

}