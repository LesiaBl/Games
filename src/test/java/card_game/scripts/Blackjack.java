package card_game.scripts;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.specification.RequestSpecification;
import io.restassured.response.Response;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;

public class Blackjack {

    private RequestSpecification baseSpec;
    Response response;

    // sets up the base specification for the API requests
    @BeforeMethod
    public void setTest(){
        baseSpec = new RequestSpecBuilder().log(LogDetail.ALL)
                .setBaseUri("https://deckofcardsapi.com/api/deck")
                .build();
    }

    @Test
    public void blackjackOperationsTest() {

        // Request a new deck from the API
        response = RestAssured.given()
                .spec(baseSpec)
                .when().get("/new/")
                .then().log().all().assertThat()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("shuffled", equalTo(false))
                .body("remaining", equalTo(52))
                .extract().response();

        // Extract the deck ID from the response
        String deckId = response.jsonPath().getString("deck_id");

        // Shuffle the deck using the API
        response = RestAssured.given()
                .spec(baseSpec)
                .when().get("/" + deckId + "/shuffle/")
                .then().log().all().assertThat()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("shuffled", equalTo(true))
                .body("remaining", equalTo(52))
                .extract().response();

        // Draw 6 cards from the deck (3 for each player)
        response = RestAssured.given()
                .spec(baseSpec)
                .when().get("/" + deckId + "/draw/?count=6")
                .then().log().all().assertThat()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("remaining", equalTo(46)) // 52 - 6 = 46 cards remaining
                .extract().response();

        // extract the cards for Player 1 and Player 2 from the response
        List<String> playerOneCards = response.jsonPath().getList("cards[0..2].code");
        System.out.println(playerOneCards);
        List<String> playerTwoCards = response.jsonPath().getList("cards[3..5].code");
        System.out.println(playerTwoCards);

        // Check if either player has a Blackjack
        boolean playerOneHasBlackjack = checkForBlackjack(playerOneCards);
        boolean playerTwoHasBlackjack = checkForBlackjack(playerTwoCards);

        // Print the results
        if (playerOneHasBlackjack && playerTwoHasBlackjack) {
            System.out.println("Both Player 1 and Player 2 have Blackjack!");
        } else if (playerOneHasBlackjack) {
            System.out.println("Player 1 has Blackjack!");
        } else if (playerTwoHasBlackjack) {
            System.out.println("Player 2 has Blackjack!");
        } else {
            System.out.println("Neither player has Blackjack.");
        }
    }

    // This method checks if the given list of cards has a total value of 21 (Blackjack)
    private boolean checkForBlackjack(List<String> cards) {
        int totalValue = 0;
        for (String card : cards) {
            switch (card.substring(0, 1)) { // Extract the value from the card code
                case "K":
                case "Q":
                case "J":
                    totalValue += 10;
                    break;
                case "A":
                    totalValue += 11;
                    break;
                default:
                    totalValue += Integer.parseInt(card.substring(0, 1));
                    break;
            }
        }
        return totalValue == 21;
    }
}
