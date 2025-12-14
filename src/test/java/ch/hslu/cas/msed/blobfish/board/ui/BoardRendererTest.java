package ch.hslu.cas.msed.blobfish.board.ui;

import ch.hslu.cas.msed.blobfish.base.Piece;
import ch.hslu.cas.msed.blobfish.base.PlayerColor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BoardRendererTest {

    BoardRenderer testee = new BoardRenderer(new TestRenderer());

    @Test
    void render_perspectiveWhite() {
        // Arrange
        var board = getTestBoard();

        // Act
        var result = testee.render(board, PlayerColor.WHITE);

        // Assert
        assertEquals(
    """
            _,_,b,_,_,_,_,_,
            r,_,k,n,_,_,_,p,
            p,_,_,_,_,_,_,_,
            _,_,P,_,p,_,_,B,
            _,_,_,_,_,_,P,_,
            _,K,_,R,_,_,_,_,
            P,_,_,_,_,r,_,_,
            _,_,_,_,_,_,N,R,
            """, result
        );
    }

    @Test
    void render_perspectiveBlack() {
        // Arrange
        var board = getTestBoard();

        // Act
        var result = testee.render(board, PlayerColor.BLACK);

        // Assert
        assertEquals(
    """
            R,N,_,_,_,_,_,_,
            _,_,r,_,_,_,_,P,
            _,_,_,_,R,_,K,_,
            _,P,_,_,_,_,_,_,
            B,_,_,p,_,P,_,_,
            _,_,_,_,_,_,_,p,
            p,_,_,_,n,k,_,r,
            _,_,_,_,_,b,_,_,
            """, result
        );

    }

    private UiBoard getTestBoard() {
        var uiFields = new UiField[][]{
                new UiField[]{emptyField(), emptyField(), field('b'), emptyField(), emptyField(), emptyField(), emptyField(), emptyField()},
                new UiField[]{field('r'), emptyField(), field('k'), field('n'), emptyField(), emptyField(), emptyField(), field('p')},
                new UiField[]{field('p'), emptyField(), emptyField(), emptyField(), emptyField(), emptyField(), emptyField(), emptyField()},
                new UiField[]{emptyField(), emptyField(), field('P'), emptyField(), field('p'), emptyField(), emptyField(), field('B')},
                new UiField[]{emptyField(), emptyField(), emptyField(), emptyField(), emptyField(), emptyField(), field('P'), emptyField()},
                new UiField[]{emptyField(), field('K'), emptyField(), field('R'), emptyField(), emptyField(), emptyField(), emptyField()},
                new UiField[]{field('P'), emptyField(), emptyField(), emptyField(), emptyField(), field('r'), emptyField(), emptyField()},
                new UiField[]{emptyField(), emptyField(), emptyField(), emptyField(), emptyField(), emptyField(), field('N'), field('R')}
        };

        var uiboard = new UiBoard();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                uiboard.set(i, j, uiFields[i][j]);
            }
        }
        return uiboard;
    }


    private static class TestRenderer implements FieldRenderer {
        @Override
        public String render(UiField field) {
            var p = field.piece();
            if (p == null) {
                return "_,";
            } else {
                return p.fen() + ",";
            }
        }
    }

    private UiField emptyField() {
        return new UiField(null, null);
    }

    private UiField field(Character fen) {
        var p = new Piece(fen);
        return new UiField(p, null);
    }

}