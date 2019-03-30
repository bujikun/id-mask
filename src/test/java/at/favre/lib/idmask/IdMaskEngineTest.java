package at.favre.lib.idmask;

import at.favre.lib.bytes.Bytes;
import org.junit.Test;

import static org.junit.Assert.*;

public class IdMaskEngineTest {
    private IdMaskEngine idMaskEngine = new IdMaskEngine.Default(Bytes.random(16).array(), Mode.MEDIUM_SIZE_AND_SECURITY);

    @Test
    public void testAllModes() {
        byte[] id = Bytes.from(397849238741629487L).array();
        for (Mode mode : Mode.values()) {
            IdMaskEngine idMaskEngine = new IdMaskEngine.Default(Bytes.from(192731092837120938L).array(), mode);
            for (int i = 0; i < 10; i++) {
                String maskedId = idMaskEngine.mask(id);
                assertNotNull(maskedId);
                assertArrayEquals(id, idMaskEngine.unmask(maskedId));
                System.out.println(maskedId);
            }
        }
    }

    @Test
    public void testShouldNotReturnSameMaskedId() {
        IdMaskEngine idMaskEngine = new IdMaskEngine.Default(Bytes.from(130984671309784536L).array(), Mode.LARGE_SIZE_AND_HIGH_SECURITY);
        byte[] id = Bytes.from(7239562391234L).array();

        String maskedId1 = idMaskEngine.mask(id);
        String maskedId2 = idMaskEngine.mask(id);
        String maskedId3 = idMaskEngine.mask(id);
        String maskedId4 = idMaskEngine.mask(id);

        assertNotEquals(maskedId1, maskedId2);
        assertNotEquals(maskedId1, maskedId3);
        assertNotEquals(maskedId1, maskedId4);
        assertNotEquals(maskedId2, maskedId3);
        assertNotEquals(maskedId3, maskedId4);

        assertArrayEquals(id, idMaskEngine.unmask(maskedId1));
        assertArrayEquals(id, idMaskEngine.unmask(maskedId2));
        assertArrayEquals(id, idMaskEngine.unmask(maskedId3));
        assertArrayEquals(id, idMaskEngine.unmask(maskedId4));
    }

    @Test
    public void testAllModesWithRandomId() {
        for (Mode mode : Mode.values()) {
            IdMaskEngine idMaskEngine = new IdMaskEngine.Default(Bytes.random(16).array(), mode);
            for (int i = 0; i < 10; i++) {
                byte[] id = Bytes.random(8).array();
                String maskedId = idMaskEngine.mask(id);
                assertNotNull(maskedId);
                assertArrayEquals(id, idMaskEngine.unmask(maskedId));
                System.out.println(maskedId);
            }
        }
    }

    @Test
    public void testVariousSizes() {
        for (Mode mode : Mode.values()) {
            IdMaskEngine idMaskEngine = new IdMaskEngine.Default(Bytes.random(16).array(), mode);
            for (int i = 1; i < 17; i++) {
                byte[] id = Bytes.random(i).array();
                String maskedId = idMaskEngine.mask(id);
                assertNotNull(maskedId);
                assertArrayEquals(id, idMaskEngine.unmask(maskedId));
                System.out.println(maskedId);
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTooLongId() {
        idMaskEngine.mask(Bytes.allocate(17).array());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testZeroLengthId() {
        idMaskEngine.mask(Bytes.empty().array());
    }

    @Test(expected = NullPointerException.class)
    public void testMaskNullInput() {
        idMaskEngine.mask(null);
    }

    @Test(expected = NullPointerException.class)
    public void testUnmaskNullInput() {
        idMaskEngine.unmask(null);
    }
}
