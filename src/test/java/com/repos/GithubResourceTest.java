package com.repos;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.empty;

@QuarkusTest
public class GithubResourceTest {

    @Test
    public void testGetReposWithBranches() {
        String user = "octocat";

        given()
                    .pathParam("user", user)
                .when()
                    .get("/github/repos/{user}")
                .then()
                    .statusCode(200)
                    .contentType(ContentType.JSON)
                    .body("size()", greaterThanOrEqualTo(0))
                    .body("[0].name", is(not(empty())))
                    .body("[0].branches[0].name", is(not(empty())));;
    }

    @Test
    public void testGetReposWithBranchesUserNotFound() {
        String user = "someNonExistingUser123";

        given()
                    .pathParam("user", user)
                .when()
                    .get("/github/repos/{user}")
                .then()
                    .statusCode(404)
                    .contentType(ContentType.JSON)
                    .body("status", is(404))
                    .body("message", is("User not found on GitHub"));
    }
}