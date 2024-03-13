import org.junit.Before;
import org.junit.Test;
import static com.google.common.truth.Truth.assertThat;

public class UnionFindTest {
    private UnionFind uf;

    @Before
    public void setup() {
        uf = new UnionFind(5);
    }

    @Test
    public void testInitialization() {
        for (int i = 0; i < 5; i++) {
            assertThat(uf.find(i)).isEqualTo(i);
        }
    }

    @Test
    public void testUnionAndConnected() {
        uf.union(0, 1);
        assertThat(uf.connected(0, 1)).isTrue();
        assertThat(uf.connected(0, 2)).isFalse();

        uf.union(2, 3);
        assertThat(uf.connected(2, 3)).isTrue();
        assertThat(uf.connected(0, 3)).isFalse();

        uf.union(1, 3);
        assertThat(uf.connected(0, 3)).isTrue();
        assertThat(uf.connected(1, 2)).isTrue();
    }

    @Test
    public void testFind() {
        uf.union(0, 1);
        uf.union(2, 3);
        uf.union(1, 3);
        int root = uf.find(0);
        for (int i = 0; i < 4; i++) {
            assertThat(uf.find(i)).isEqualTo(root);
        }
        assertThat(uf.find(4)).isNotEqualTo(root);
    }

    @Test
    public void testSizeOf() {
        uf.union(0, 1);
        uf.union(2, 3);
        assertThat(uf.sizeOf(0)).isEqualTo(2);
        assertThat(uf.sizeOf(3)).isEqualTo(2);

        uf.union(1, 3);
        for (int i = 0; i < 4; i++) {
            assertThat(uf.sizeOf(i)).isEqualTo(4);
        }
    }
}
