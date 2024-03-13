import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;

public class ArrayDequeTest {

    @Test
    /** In this test, we have three different assert statements that verify that addFirst works correctly. */
    public void addFirstTestBasic() {
        Deque<String> ad1 = new ArrayDeque<>();

        ad1.addFirst("back"); // after this call we expect: ["back"]
        assertThat(ad1.toList()).containsExactly("back").inOrder();

        ad1.addFirst("middle"); // after this call we expect: ["middle", "back"]
        assertThat(ad1.toList()).containsExactly("middle", "back").inOrder();

        ad1.addFirst("front"); // after this call we expect: ["front", "middle", "back"]
        assertThat(ad1.toList()).containsExactly("front", "middle", "back").inOrder();

         /* Note: The first two assertThat statements aren't really necessary. For example, it's hard
            to imagine a bug in your code that would lead to ["front"] and ["front", "middle"] failing,
            but not ["front", "middle", "back"].
          */
    }

    @Test
    /** In this test, we use only one assertThat statement. IMO this test is just as good as addFirstTestBasic.
     *  In other words, the tedious work of adding the extra assertThat statements isn't worth it. */
    public void addLastTestBasic() {
        Deque<String> ad1 = new ArrayDeque<>();

        ad1.addLast("front"); // after this call we expect: ["front"]
        ad1.addLast("middle"); // after this call we expect: ["front", "middle"]
        ad1.addLast("back"); // after this call we expect: ["front", "middle", "back"]
        assertThat(ad1.toList()).containsExactly("front", "middle", "back").inOrder();
    }

    @Test
    /** This test performs interspersed addFirst and addLast calls. */
    public void addFirstAndAddLastTest() {
        Deque<Integer> ad1 = new ArrayDeque<>();

         /* I've decided to add in comments the state after each call for the convenience of the
            person reading this test. Some programmers might consider this excessively verbose. */
        ad1.addLast(0);   // [0]
        ad1.addLast(1);   // [0, 1]
        ad1.addFirst(-1); // [-1, 0, 1]
        ad1.addLast(2);   // [-1, 0, 1, 2]
        ad1.addFirst(-2); // [-2, -1, 0, 1, 2]

        assertThat(ad1.toList()).containsExactly(-2, -1, 0, 1, 2).inOrder();
    }

    // Below, you'll write your own tests for LinkedListDeque.

    @Test
    public void getTest() {
        Deque<String> ad1 = new ArrayDeque<>();

        ad1.addLast("A");
        ad1.addLast("B");
        ad1.addLast("C");

        // Test valid
        assertThat(ad1.get(0)).isEqualTo("A");
        assertThat(ad1.get(1)).isEqualTo("B");
        assertThat(ad1.get(2)).isEqualTo("C");

        // Test invalid
        assertThat(ad1.get(-1)).isEqualTo(null);
        assertThat(ad1.get(3)).isEqualTo(null);
        assertThat(ad1.get(10)).isEqualTo(null);
    }

    @Test
    public void removeFirstTest() {
        Deque<Integer> ad1 = new ArrayDeque<>();

        assertThat(ad1.size()).isEqualTo(0);
        ad1.addLast(0);   // [0]
        ad1.addLast(1);   // [0, 1]
        ad1.addFirst(-1); // [-1, 0, 1]
        ad1.addLast(2);   // [-1, 0, 1, 2]
        ad1.addFirst(-2); // [-2, -1, 0, 1, 2]

        assertThat(ad1.toList()).containsExactly(-2, -1, 0, 1, 2).inOrder();
        assertThat(ad1.size()).isEqualTo(5);
        ad1.removeFirst();
        assertThat(ad1.toList()).containsExactly(-1, 0, 1, 2).inOrder();
        ad1.removeFirst();
        ad1.removeFirst();
        ad1.removeFirst();
        assertThat(ad1.toList()).containsExactly(2).inOrder();
        assertThat(ad1.isEmpty()).isEqualTo(false);
        ad1.removeFirst();
        assertThat(ad1.toList()).containsExactly().inOrder();
        assertThat(ad1.size()).isEqualTo(0);
    }

    @Test
    public void removeLastTest() {
        Deque<Integer> ad1 = new ArrayDeque<>();

        ad1.addLast(0);   // [0]
        ad1.addLast(1);   // [0, 1]
        ad1.addFirst(-1); // [-1, 0, 1]
        ad1.addLast(2);   // [-1, 0, 1, 2]
        ad1.addFirst(-2); // [-2, -1, 0, 1, 2]

        assertThat(ad1.toList()).containsExactly(-2, -1, 0, 1, 2).inOrder();
        ad1.removeLast();
        assertThat(ad1.toList()).containsExactly(-2, -1, 0, 1).inOrder();
        ad1.removeLast();
        ad1.removeLast();
        ad1.removeLast();
        assertThat(ad1.toList()).containsExactly(-2).inOrder();
        assertThat(ad1.isEmpty()).isEqualTo(false);
        ad1.removeLast();
        assertThat(ad1.toList()).containsExactly().inOrder();
        assertThat(ad1.isEmpty()).isEqualTo(true);
    }

    @Test
    public void addAfterRemoveTest() {
        Deque<Integer> ad1 = new ArrayDeque<>();

        //add first and remove to empty
        ad1.addFirst(5);
        ad1.addFirst(4);
        ad1.addFirst(3);
        ad1.addFirst(2);
        ad1.addFirst(1);

        assertThat(ad1.toList()).containsExactly(1, 2, 3, 4, 5).inOrder();

        ad1.removeFirst();
        ad1.removeFirst();
        ad1.removeFirst();
        ad1.removeFirst();
        ad1.removeFirst();

        assertThat(ad1.toList()).containsExactly().inOrder();
        assertThat(ad1.isEmpty()).isEqualTo(true);

        //add last and remove to empty
        ad1.addLast(4);
        ad1.addLast(5);
        ad1.addFirst(3);
        ad1.addFirst(2);
        ad1.addFirst(1);

        assertThat(ad1.toList()).containsExactly(1, 2, 3, 4, 5).inOrder();

        ad1.removeLast();
        ad1.removeLast();
        ad1.removeLast();
        ad1.removeLast();
        ad1.removeLast();

        assertThat(ad1.toList()).containsExactly().inOrder();
        assertThat(ad1.isEmpty()).isEqualTo(true);

    }

    @Test
    public void addFirstAfterTriggerResizeTest() {
        Deque<Integer> ad1 = new ArrayDeque<>();

        ad1.addFirst(10);
        ad1.addFirst(9);
        ad1.addFirst(8);
        ad1.addFirst(7);
        ad1.addFirst(6);
        ad1.addFirst(5);
        ad1.addFirst(4);
        ad1.addFirst(3);
        ad1.addFirst(2);
        ad1.addFirst(1);

        assertThat(ad1.toList()).containsExactly(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).inOrder();

        ad1.removeFirst();
        ad1.removeFirst();
        ad1.removeFirst();
        ad1.removeFirst();
        ad1.removeFirst();
        ad1.removeFirst();
        ad1.removeFirst();
        ad1.removeFirst();
        ad1.removeFirst();
        ad1.removeFirst();

        assertThat(ad1.toList()).containsExactly().inOrder();
        assertThat(ad1.isEmpty()).isEqualTo(true);
    }

    @Test
    public void addLastAfterTriggerResizeTest() {
        Deque<Integer> ad1 = new ArrayDeque<>();

        ad1.addLast(1);
        ad1.addLast(2);
        ad1.addLast(3);
        ad1.addLast(4);
        ad1.addLast(5);
        ad1.addLast(6);
        ad1.addLast(7);
        ad1.addLast(8);
        ad1.addLast(9);
        ad1.addLast(10);

        assertThat(ad1.toList()).containsExactly(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).inOrder();

        ad1.removeLast();
        ad1.removeLast();
        ad1.removeLast();
        ad1.removeLast();
        ad1.removeLast();
        ad1.removeLast();
        ad1.removeLast();
        ad1.removeLast();
        ad1.removeLast();
        ad1.removeLast();

        assertThat(ad1.toList()).containsExactly().inOrder();
        assertThat(ad1.isEmpty()).isEqualTo(true);
    }

    @Test
    public void removeFirstTriggerResizeTest() {
        Deque<Integer> ad1 = new ArrayDeque<>();

        for (int i = 0; i < 1000; i++) {
            ad1.addFirst(i);
        }
        for (int i = 999; i >= 0; i--) {
            ad1.removeFirst();
        }
        assertThat(ad1.isEmpty()).isEqualTo(true);



    }

    @Test
    public void addFirstAfterRemoveToEmptyTest(){
        Deque<Integer> ad1 = new ArrayDeque<>();

        for (int i = 0; i < 10; i++) {
            ad1.addFirst(i);
        }
        for (int i = 9; i >= 0; i--) {
            ad1.removeFirst();
        }
        assertThat(ad1.isEmpty()).isEqualTo(true);

        for (int i = 0; i < 6; i++) {
            ad1.addFirst(i);
        }
        assertThat(ad1.toList()).containsExactly(5, 4, 3, 2, 1, 0).inOrder();
    }
}
