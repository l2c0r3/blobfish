package ch.hslu.cas.msed.blobfish.base.fen;

import ch.hslu.cas.msed.blobfish.base.FenUtil;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class FenUtilTest {

    @ParameterizedTest
    @ValueSource(strings = {
            "a1bk3r/p2pBpNp/n4n2/1p1NP2P/6P1/3P4/P1P1K3/q5b1",             // invalid character (first char)
            "9/p2pBpNp/n4n2/1p1NP2P/6P1/3P4/P1P1K3/q5b1",                  // too high number
            "6pppp/p2pBpNp/n4n2/1p1NP2P/6P1/3P4/P1P1K3/q5b1",              // mix of pieces and number is too high
            "1pp/p2pBpNp/n4n2/1p1NP2P/6P1/3P4/P1P1K3/q5b1",                // mix of pieces and number is too little
            "p/p2pBpNp/8/8/8/8/PPPPPPPP/RNBQKBNR",                         // too little pieces
            "ppppppppp/p2pBpNp/8/8/8/8/PPPPPPPP/RNBQKBNR",                 // too many pieces
            "pppppppp/p2pBpNp/8/8/8/8/PPPPPPPP",                           // too little blocks
            "ppppppppp/p2pBpNp/8/8/8/8/PPPPPPPP/RNBQKBNR/ppppppppp/",      // too much blocks
    })
    @NullAndEmptySource
    void parse_invalidFenString_throwsException(String position) {
        // Act & assert
        assertThrows(IllegalArgumentException.class, () -> FenUtil.isFenStringValid(position));
    }

}