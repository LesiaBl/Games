package checkers.scripts;

import checkers.pages.CheckersPage;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class CheckersGame extends Base {

    @BeforeMethod
    public void setPage() {
        // 1. Navigate to the Checkers game website
        driver.get("https://www.gamesforthebrain.com/game/checkers/");

        // 2. Confirm that the site is up by checking the URL
        Assert.assertTrue(driver.getCurrentUrl().contains("/game/checkers/"));

        // initialize the CheckersPage object
        checkersPage = new CheckersPage();
    }

    @Test
    public void checkersGameAutomation() {
        // Define the  5 moves to be made
        int[][][] moves = {
                {{6, 2}, {5, 3}},
                {{2, 2}, {3, 3}},
                {{1, 1}, {2, 2}},
                {{2, 2}, {0, 4}},
                {{0, 2}, {2, 4}}
        };

        // Count the initial number of blue pieces on the board
        int bluePiecesCount = checkersPage.bluePieces.size();

        // Counter for the number of captured blue pieces
        int capturedBluePiecesCount = 0;

        // Loop through the defined moves and execute them
        for (int[][] move : moves) {

            // Make a move on the board
            checkersPage.makeMove(move[0], move[1]);

            // Check if a blue piece was captured during the move
            if (bluePiecesCount > checkersPage.bluePieces.size()) {
                capturedBluePiecesCount++;
                int diffX = Math.abs(move[0][0] - move[1][0]);
                int diffY = Math.abs(move[0][1] - move[1][1]);

                // Assert that the move was a capturing move
                Assert.assertTrue(diffX == 2 && diffY == 2);
            }

            // assert that the move was legal by checking the message
            Assert.assertEquals(checkersPage.message.getText(), "Make a move.");
        }

        // assert that the number of captured blue pieces matches the difference in count
        Assert.assertEquals(bluePiecesCount, checkersPage.bluePieces.size() + capturedBluePiecesCount);

        // restart the game
        checkersPage.restart.click();

        // assert that the game was restarted successfully by checking the message
        Assert.assertEquals(checkersPage.message.getText(), "Select an orange piece to move.");
    }
}
