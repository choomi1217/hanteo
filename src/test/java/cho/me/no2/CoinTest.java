package cho.me.no2;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class CoinTest {

    @DisplayName("Test countWaysToMakeChange with various inputs")
    @ParameterizedTest(name = "{index} => sum={0}, coins={1}, expectedWays={2}")
    @MethodSource("provideTestCases")
    void testCountWaysToMakeChange(int sum, int[] coins, int expectedWays) {
        int actualWays = Coin.countWaysToMakeChange(sum, coins);
        assertThat(actualWays).isEqualTo(expectedWays);
    }

    private static Stream<Arguments> provideTestCases() {
        return Stream.of(
                Arguments.of(5, new int[]{1, 2, 5}, 4),
                Arguments.of(3, new int[]{2}, 0),
                Arguments.of(10, new int[]{1, 2, 3}, 14),
                Arguments.of(0, new int[]{1, 2, 3}, 1),
                Arguments.of(4, new int[]{1, 2, 3}, 4)
        );
    }
}