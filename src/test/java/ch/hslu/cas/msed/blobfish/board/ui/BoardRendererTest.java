package ch.hslu.cas.msed.blobfish.board.ui;

import ch.hslu.cas.msed.blobfish.base.Piece;
import ch.hslu.cas.msed.blobfish.base.PlayerColor;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BoardRendererTest {

    BoardRenderer testee = new BoardRenderer(new TestFieldRenderer(), new TestBorderFieldRenderer());

    @Test
    void render_perspectiveWhite() {
        // Arrange
        var board = getTestBoard();

        // Act
        var result = testee.render(board, PlayerColor.WHITE);

        // Assert
        assertEquals(
    """
            _,A,B,C,D,E,F,G,H,_
            8,_,_,b,_,_,_,_,_,8,
            7,r,_,k,n,_,_,_,p,7,
            6,p,_,_,_,_,_,_,_,6,
            5,_,_,P,_,p,_,_,B,5,
            4,_,_,_,_,_,_,P,_,4,
            3,_,K,_,R,_,_,_,_,3,
            2,P,_,_,_,_,r,_,_,2,
            1,_,_,_,_,_,_,N,R,1,
            _,A,B,C,D,E,F,G,H,_
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
            _,H,G,F,E,D,C,B,A,_
            1,R,N,_,_,_,_,_,_,1,
            2,_,_,r,_,_,_,_,P,2,
            3,_,_,_,_,R,_,K,_,3,
            4,_,P,_,_,_,_,_,_,4,
            5,B,_,_,p,_,P,_,_,5,
            6,_,_,_,_,_,_,_,p,6,
            7,p,_,_,_,n,k,_,r,7,
            8,_,_,_,_,_,b,_,_,8,
            _,H,G,F,E,D,C,B,A,_
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


    private static class TestFieldRenderer implements FieldRenderer {
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

    private static class TestBorderFieldRenderer implements BorderFieldRenderer {
        @Override
        public String renderRow(PlayerColor perspective) {
            var row = "_,A,B,C,D,E,F,G,H,_";
            return switch (perspective) {
                case WHITE -> row;
                case BLACK -> StringUtils.reverse(row);
            };
        }

        @Override
        public String renderColumnField(int colNumber) {
            return colNumber + ",";
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