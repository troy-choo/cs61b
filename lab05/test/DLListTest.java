import org.junit.Test;
import static com.google.common.truth.Truth.assertWithMessage;
import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertTrue;

public class DLListTest {

    @Test
    public void testDLListAdd() {
        DLListFactory<Integer> factory = new DLListFactory<>();
        DLList<Integer> test1 = factory.of(1, 3, 5);
        DLList<Integer> test2 = new DLList<>();

        test1.add(1, 2);
        test1.add(3, 4);
        assertWithMessage("Size was not equal to 5").that(test1.size()).isEqualTo(5);
        assertWithMessage("Second item was not 3").that(test1.get(2)).isEqualTo(3);
        assertWithMessage("Third item was not 4").that(test1.get(3)).isEqualTo(4);

        test2.add(1, 1);
        assertWithMessage("Singleton does not contain 1").that(test2.get(0)).isEqualTo(1);
        assertWithMessage("Singleton does not have a size of 1").that(test2.size()).isEqualTo(1);

        test2.add(10, 10);
        assertWithMessage("test2 does not have 10 at the first index").that(test2.get(1)).isEqualTo(10);
        test1.add(0, 0);
        assertWithMessage("list is incorrect after addition at the front").that(test1.equals(factory.of(0, 1, 2, 3, 4, 5))).isTrue();
    }

    @Test
    public void testReverseGeneralCase() {
        DLList<Integer> list = new DLList<>();
        list.addFirst(1);
        list.addFirst(2);
        list.addFirst(3);
        DLList<Integer> expected = new DLList<>();
        expected.addFirst(3);
        expected.addFirst(2);
        expected.addFirst(1);

        list.reverse();

        assertEquals(expected, list);
    }

    @Test
    public void testReverseBaseCaseSize1() {
        DLList<Integer> list = new DLList<>();
        list.addFirst(1);
        DLList<Integer> expected = new DLList<>();
        expected.addFirst(1);

        list.reverse();

        assertTrue(list.equals(expected));
    }

    @Test
    public void testReverseBaseCaseSize0() {
        DLList<Integer> list = new DLList<>();
        DLList<Integer> expected = new DLList<>();

        list.reverse();

        assertTrue(list.equals(expected));
    }
}
