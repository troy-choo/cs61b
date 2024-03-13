import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertArrayEquals;

public class DistributionSortsTest {

    @Test
    public void testCountingSort() {
        int[] arr1 = {3, 6, 1, 7, 2, 8, 4, 0, 5, 9};
        DistributionSorts.countingSort(arr1);
        assertArrayEquals(new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9}, arr1);

        int[] arr2 = {9, 9, 8, 8, 7, 7, 6, 6, 0, 0};
        DistributionSorts.countingSort(arr2);
        assertArrayEquals(new int[]{0, 0, 6, 6, 7, 7, 8, 8, 9, 9}, arr2);

        int[] arr3 = {0, 0, 0, 0, 0};
        DistributionSorts.countingSort(arr3);
        assertArrayEquals(new int[]{0, 0, 0, 0, 0}, arr3);
    }

    @Test
    public void testLSDRadixSort() {
        int[] arr1 = {333, 222, 111, 666, 555, 444, 999, 888, 777, 0};
        DistributionSorts.lsdRadixSort(arr1);
        assertArrayEquals(new int[]{0, 111, 222, 333, 444, 555, 666, 777, 888, 999}, arr1);

        int[] arr2 = {9, 99, 999, 9999, 8, 88, 888, 8888, 7, 77};
        DistributionSorts.lsdRadixSort(arr2);
        assertArrayEquals(new int[]{7, 8, 9, 77, 88, 99, 888, 999, 8888, 9999}, arr2);

        int[] arr3 = {0, 0, 0, 0, 0};
        DistributionSorts.lsdRadixSort(arr3);
        assertArrayEquals(new int[]{0, 0, 0, 0, 0}, arr3);
    }
}

