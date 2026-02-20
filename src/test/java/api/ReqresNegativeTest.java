package api;

import api.DTO.FailReg;
import api.DTO.Register;
import api.DTO.Token;
import api.config.Configuration;
import api.config.Specifications;
import io.restassured.response.ValidatableResponse;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReqresNegativeTest {

    private static final Configuration config = ConfigFactory.create(Configuration.class);

    @ParameterizedTest
    @CsvFileSource(resources = "/testdata/login_negative_cases_eveholt.csv") // POST Verify session
    public void negativeSessionTest(String email,
                                  String password,
                                  int expectedStatus,
                                  String expectedToken,
                                  String expectedErrorMessage) {
        Specifications.installSpecification(
                Specifications.requestSpec(),
                Specifications.responseSpec(config.status400()));

        Register regData = new Register(email, password);

        ValidatableResponse response = given()
                .baseUri(config.urlReqresApi())
                .header("x-api-key", config.apiToken())
                .body(regData)
                .when()
                .post(config.login())
                .then()
                .log().all();

        if (expectedStatus == config.status400()) {
            Token actualToken = response.extract().as(Token.class);
            assertEquals(expectedToken, actualToken.getToken());
        } else {
            FailReg error = response.extract().as(FailReg.class);
            assertEquals(expectedErrorMessage, error.getError());
        }
    }
}
