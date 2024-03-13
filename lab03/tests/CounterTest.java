import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertWithMessage;
class CounterTest {

    @Test
    void testIncrement() {
        Counter c = new Counter();
        c.increment();
        assertWithMessage("Counter value is not 1.").that(c.value()).isEqualTo(1);
        c.increment();
        assertWithMessage("Counter value is not 2.").that(c.value()).isEqualTo(2);
    }

    @Test
    void testReset() {
        Counter c = new Counter();
        c.increment();
        c.reset();
        assertWithMessage("Counter value is not 0 after reset.").that(c.value()).isEqualTo(0);
    }

    @Test
    public void testConstructor() {
        Counter c = new Counter();
        assertWithMessage("Counter value is not 0 upon instantiation.").that(c.value()).isEqualTo(0);
    }
}