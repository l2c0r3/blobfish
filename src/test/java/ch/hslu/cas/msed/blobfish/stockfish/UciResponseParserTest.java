package ch.hslu.cas.msed.blobfish.stockfish;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class UciResponseParserTest {

    private UciResponseParser testee = new UciResponseParser();

    @Test
    void parseBestMoves_parsesBestNextMoves() {
        // Arrange
        String[] uciResponse = {
                "info string Available processors: 0-23",
                "info string Using 1 thread",
                "info string NNUE evaluation using nn-c288c895ea92.nnue (125MiB, (102384, 1024, 15, 32, 1))",
                "info string NNUE evaluation using nn-37f18f62d772.nnue (6MiB, (22528, 128, 15, 32, 1))",
                "info string Network replica 1: Local memory. Shared memory not supported by the OS. Local allocation fallback.",
                "info depth 1 seldepth 4 multipv 1 score cp 235 nodes 65 nps 65000 hashfull 0 tbhits 0 time 1 pv g8g5",
                "info depth 2 seldepth 4 multipv 1 score cp 250 nodes 132 nps 132000 hashfull 0 tbhits 0 time 1 pv g8g5 g3g5 h6g5",
                "info depth 3 seldepth 4 multipv 1 score cp 250 nodes 184 nps 184000 hashfull 0 tbhits 0 time 1 pv g8g5 g3g5 h6g5",
                "info depth 4 seldepth 5 multipv 1 score cp 383 nodes 265 nps 265000 hashfull 0 tbhits 0 time 1 pv g8g5 c1g5 h6g5",
                "info depth 5 seldepth 7 multipv 1 score cp 434 nodes 489 nps 489000 hashfull 0 tbhits 0 time 1 pv g8g5 c1f4 g5g3 c2c5 d6c",
                "bestmove g8g5 ponder c1f4"
        };

        // Act
        var response = testee.parseBestMoves(uciResponse);

        // Assert
        assertFalse(response.isEmpty());
        assertEquals(1, response.size());
        assertEquals("g8g5", response.getFirst());
    }

    @Test
    void parseBestMoves_parsesBest3NextMoves() {
        // Arrange
        String[] uciResponse = {
                "info string Available processors: 0-23",
                "info string Using 1 thread",
                "info string NNUE evaluation using nn-c288c895ea92.nnue (125MiB, (102384, 1024, 15, 32, 1))",
                "info string NNUE evaluation using nn-37f18f62d772.nnue (6MiB, (22528, 128, 15, 32, 1))",
                "info string Network replica 1: Local memory. Shared memory not supported by the OS. Local allocation fallback.",
                "info depth 1 seldepth 4 multipv 1 score cp 235 nodes 351 nps 351000 hashfull 0 tbhits 0 time 1 pv g8g5",
                "info depth 1 seldepth 7 multipv 2 score cp 227 nodes 351 nps 351000 hashfull 0 tbhits 0 time 1 pv e5g4 h3g4",
                "info depth 1 seldepth 5 multipv 3 score cp 222 nodes 351 nps 351000 hashfull 0 tbhits 0 time 1 pv e5f3 g2f3 g8g5",
                "info depth 2 seldepth 4 multipv 1 score cp 288 nodes 567 nps 567000 hashfull 0 tbhits 0 time 1 pv f6g5",
                "info depth 2 seldepth 5 multipv 2 score cp 255 nodes 567 nps 567000 hashfull 0 tbhits 0 time 1 pv e5f3 g2f3 g8g5",
                "info depth 2 seldepth 4 multipv 3 score cp 254 nodes 567 nps 567000 hashfull 0 tbhits 0 time 1 pv g8g5 g3g5 h6g5",
                "info depth 3 seldepth 6 multipv 1 score cp 394 nodes 764 nps 764000 hashfull 0 tbhits 0 time 1 pv f6g5",
                "info depth 3 seldepth 4 multipv 2 score cp 257 nodes 764 nps 764000 hashfull 0 tbhits 0 time 1 pv g8g5 g3g5 h6g5",
                "info depth 3 seldepth 5 multipv 3 score cp 255 nodes 764 nps 764000 hashfull 0 tbhits 0 time 1 pv e5f3 g2f3 g8g5",
                "info depth 4 seldepth 6 multipv 1 score cp 484 nodes 1029 nps 1029000 hashfull 0 tbhits 0 time 1 pv f6g5 c1g5 g8g5 g3g5 h6g5",
                "info depth 4 seldepth 4 multipv 2 score cp 261 nodes 1029 nps 1029000 hashfull 0 tbhits 0 time 1 pv g8g5 g3g5 h6g5",
                "info depth 4 seldepth 5 multipv 3 score cp 255 nodes 1029 nps 1029000 hashfull 0 tbhits 0 time 1 pv e5f3 g2f3 g8g5",
                "info depth 5 seldepth 7 multipv 1 score cp 483 nodes 1421 nps 1421000 hashfull 0 tbhits 0 time 1 pv g8g5 g3g5 h6g5",
                "info depth 5 seldepth 7 multipv 2 score cp 473 nodes 1421 nps 1421000 hashfull 0 tbhits 0 time 1 pv f6g5 c1g5 g8g5 c2c1 g5g3",
                "info depth 5 seldepth 6 multipv 3 score cp 352 nodes 1421 nps 1421000 hashfull 0 tbhits 0 time 1 pv e5f3 g2f3 g8g5 c1g5 h6g5",
                "bestmove g8g5 ponder g3g5"
        };

        // Act
        var response = testee.parseBestMoves(uciResponse);

        // Assert
        assertFalse(response.isEmpty());
        assertEquals(3, response.size());
        assertEquals("g8g5", response.getFirst());
        assertEquals("f6g5", response.get(1));
        assertEquals("e5f3", response.get(2));
    }


    // position fen 1k6/ppp5/8/8/8/8/PPPP4/1K2R3 w - - 0 1
    @Test
    void parseBestMoves_mate_best_3_moves() {
        // Arrange
        String[] uciResponse = {
                "info string Available processors: 0-23",
                "info string Using 1 thread",
                "info string NNUE evaluation using nn-c288c895ea92.nnue (125MiB, (102384, 1024, 15, 32, 1))",
                "info string NNUE evaluation using nn-37f18f62d772.nnue (6MiB, (22528, 128, 15, 32, 1))",
                "info string Network replica 1: Local memory. Shared memory not supported by the OS. Local allocation fallback.",
                "info depth 1 seldepth 2 multipv 1 score mate 1 nodes 79 nps 79000 hashfull 0 tbhits 0 time 1 pv e1e8",
                "info depth 1 seldepth 3 multipv 2 score cp 727 nodes 79 nps 79000 hashfull 0 tbhits 0 time 1 pv b2b3 c7c5",
                "info depth 1 seldepth 3 multipv 3 score cp 694 nodes 79 nps 79000 hashfull 0 tbhits 0 time 1 pv a2a3 c7c5",
                "info depth 2 seldepth 2 multipv 1 score mate 1 nodes 211 nps 211000 hashfull 0 tbhits 0 time 1 pv e1e8",
                "info depth 2 seldepth 4 multipv 2 score cp 757 nodes 211 nps 211000 hashfull 0 tbhits 0 time 1 pv b2b4 c7c5",
                "info depth 2 seldepth 3 multipv 3 score cp 745 nodes 211 nps 211000 hashfull 0 tbhits 0 time 1 pv c2c3 c7c5",
                "info depth 3 seldepth 2 multipv 1 score mate 1 nodes 298 nps 298000 hashfull 0 tbhits 0 time 1 pv e1e8",
                "info depth 3 seldepth 4 multipv 2 score cp 745 nodes 298 nps 298000 hashfull 0 tbhits 0 time 1 pv c2c3",
                "info depth 3 seldepth 4 multipv 3 score cp 731 nodes 298 nps 298000 hashfull 0 tbhits 0 time 1 pv b2b4 c7c5 b4c5",
                "info depth 4 seldepth 2 multipv 1 score mate 1 nodes 467 nps 467000 hashfull 0 tbhits 0 time 1 pv e1e8",
                "info depth 4 seldepth 10 multipv 2 score cp 774 nodes 467 nps 467000 hashfull 0 tbhits 0 time 1 pv b2b4 b7b6",
                "info depth 4 seldepth 5 multipv 3 score cp 741 nodes 467 nps 467000 hashfull 0 tbhits 0 time 1 pv c2c3 c7c5 c3c4 b8a8",
                "info depth 5 seldepth 2 multipv 1 score mate 1 nodes 718 nps 718000 hashfull 0 tbhits 0 time 1 pv e1e8",
                "info depth 5 seldepth 6 multipv 2 score cp 751 nodes 718 nps 718000 hashfull 0 tbhits 0 time 1 pv b2b4 a7a5 b4a5",
                "info depth 5 seldepth 6 multipv 3 score cp 741 nodes 718 nps 718000 hashfull 0 tbhits 0 time 1 pv c2c3",
                "bestmove e1e8"
        };

        // Act
        var response = testee.parseBestMoves(uciResponse);

        // Assert
        assertFalse(response.isEmpty());
        assertEquals(3, response.size());
        assertEquals("e1e8", response.getFirst());
        assertEquals("b2b4", response.get(1));
        assertEquals("c2c3", response.get(2));
    }

}