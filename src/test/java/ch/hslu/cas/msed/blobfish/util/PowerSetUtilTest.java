package ch.hslu.cas.msed.blobfish.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PowerSetUtilTest {

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5})
    void getPermutation_sizeIsCorrect(int n) {
        // Arrange
        var testList = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            testList.add(i);
        }

        // Act
        var result = PowerSetUtil.getPermutation(testList.toArray());

        // Act
        var expected = Math.pow(2, n) - 1; // without empty list
        assertEquals(expected, result.size());
    }

    @Test
    void getPermutation_contentIsCorrect_2() {
        // Arrange
        var testList = List.of("a", "b");

        // Act
        var result = PowerSetUtil.getPermutation(testList.toArray());

        // Assert
        assertTrue(result.contains(List.of("a")));
        assertTrue(result.contains(List.of("a", "b")));
        assertTrue(result.contains(List.of("b")));
    }

    @Test
    void getPermutation_contentIsCorrect_3() {
        // Arrange
        var testList = List.of("a", "b", "c");

        // Act
        var result = PowerSetUtil.getPermutation(testList.toArray());

        // Assert
        assertEquals(6, result.size());
        assertTrue(result.contains(List.of("a")));
        assertTrue(result.contains(List.of("a", "b")));
        assertTrue(result.contains(List.of("a", "b", "c")));
        assertTrue(result.contains(List.of("a", "c")));
        assertTrue(result.contains(List.of("b")));
        assertTrue(result.contains(List.of("b", "c")));
        assertTrue(result.contains(List.of("c")));
    }

    @Test
    void getPermutation_contentIsCorrect_5() {
        // Arrange
        var testList = List.of("a", "b", "c", "d", "e");

        // Act
        var result = PowerSetUtil.getPermutation(testList.toArray());

        // Assert
        assertTrue(result.contains(List.of("a")));
        assertTrue(result.contains(List.of("a", "b")));
        assertTrue(result.contains(List.of("a", "b", "c")));
        assertTrue(result.contains(List.of("a", "b", "c", "d")));
        assertTrue(result.contains(List.of("a", "b", "c", "d", "e")));
        assertTrue(result.contains(List.of("a", "c")));
        assertTrue(result.contains(List.of("a", "c", "d")));
        assertTrue(result.contains(List.of("a", "c", "d", "e")));
        assertTrue(result.contains(List.of("a", "d")));
        assertTrue(result.contains(List.of("a", "d", "e")));
        assertTrue(result.contains(List.of("a", "e")));
        assertTrue(result.contains(List.of("b")));
        assertTrue(result.contains(List.of("b", "c")));
        assertTrue(result.contains(List.of("b", "c", "d")));
        assertTrue(result.contains(List.of("b", "c", "d", "e")));
        assertTrue(result.contains(List.of("b", "d")));
        assertTrue(result.contains(List.of("b", "d", "e")));
        assertTrue(result.contains(List.of("b", "e")));
        assertTrue(result.contains(List.of("c")));
        assertTrue(result.contains(List.of("c", "d")));
        assertTrue(result.contains(List.of("c", "d", "e")));
        assertTrue(result.contains(List.of("c", "e")));
        assertTrue(result.contains(List.of("d")));
        assertTrue(result.contains(List.of("d", "e")));
        assertTrue(result.contains(List.of("e")));
    }

}