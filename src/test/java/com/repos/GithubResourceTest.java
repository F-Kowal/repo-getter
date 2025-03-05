package com.repos;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class GithubResourceTest {

    @Test
    public void testGetReposWithBranches() {

        String user = "octocat";

        List<Map<String, Object>> response = given()
                    .contentType(ContentType.JSON)
                .when()
                    .get("/github/repos-with-branches/" + user)
                .then()
                    .statusCode(200)
                    .extract()
                    .as(new TypeRef<>() {});

        assertNotNull(response);
        assertFalse(response.isEmpty());

        for (Map<String, Object> repo : response) {
            assertTrue(repo.containsKey("repo_name"));
            assertTrue(repo.containsKey("owner_login"));
            assertTrue(repo.containsKey("branches_in_repo"));

            List<Map<String, Object>> branches = (List<Map<String, Object>>) repo.get("branches_in_repo");
            assertFalse(branches.isEmpty());

            for (Map<String, Object> branch : branches) {
                assertTrue(branch.containsKey("branch_name"));
                assertTrue(branch.containsKey("last_commit_sha"));
            }
        }
    }

    @Test
    public void testGetReposWithBranchesUserNotFound() {

        String user = "someNonExistingUser123";

        given()
                    .contentType(ContentType.JSON)
                .when()
                    .get("/github/repos-with-branches/" + user)
                .then()
                    .statusCode(404)
                    .body("status", equalTo(404))
                    .body("message", equalTo("User not found on GitHub"));
    }
}