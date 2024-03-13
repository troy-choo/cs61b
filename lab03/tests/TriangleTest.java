import org.junit.Rule;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;
public abstract class TriangleTest {

    /** For autograding purposes; do not change this line. */
    abstract Triangle getNewTriangle();

    /* ***** TESTS ***** */

    // FIXME: Add additional tests for Triangle.java here that pass on a
    //  correct Triangle implementation and fail on buggy Triangle implementations.

    @Test
    public void testSidesFormTriangle() {
        // TODO: stub for first test
        Triangle t = getNewTriangle();
        // remember that you'll have to call on Triangle methods like
        // t.functionName(arguments), where t is a Triangle object
        assertThat(t.sidesFormTriangle(3, 4, 5)).isEqualTo(true);
        assertThat(t.sidesFormTriangle(1, 2, 10)).isEqualTo(false);

    }

    @Test
    public void testPointsFormTriangle(){
        Triangle t = getNewTriangle();
        assertThat(t.pointsFormTriangle(0, 0, 3, 0, 3, 4)).isEqualTo(true);
        assertThat(t.pointsFormTriangle(0, 0, 1, 0, 2, 0)).isEqualTo(false);
    }

    @Test
    public void testTriangleType(){
        Triangle t = getNewTriangle();
        assertThat(t.triangleType(3, 4, 5)).isEqualTo("Scalene");
        assertThat(t.triangleType(5, 5, 7)).isEqualTo("Isosceles");
        assertThat(t.triangleType(3, 3, 3)).isEqualTo("Equilateral");
    }

    @Test
    public void testSquaredHypotenuse(){
        Triangle t = getNewTriangle();
        assertThat(t.squaredHypotenuse(1, 2)).isEqualTo(5);
    }
}
