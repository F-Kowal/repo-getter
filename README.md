# repo-getter

## Description

The **repo-getter** is a tool that allows you to easily retrieve all public repositories created by a specific GitHub user, excluding any forked repos.  
For each of these repositories, it also provides a list of branches along with the latest commit, all in a clean and simple JSON format. The app uses the GitHub API to gather this data, ensuring an organized and user-friendly output.

Example:
```json
[
  {
    "repo_name": "some-repo",
    "owner_login": "someUser",
    "branches_in_repo": [
      {
        "branch_name": "main",
        "last_commit_sha": "abcdef1234567890"
      },
      {
        "branch_name": "dev",
        "last_commit_sha": "123456abcdef7890"
      }
    ]
  }
]
```

The application works entirely as a REST API, providing a simple and fast way to browse GitHub repositories and branches without the need to log into the platform.

## Table of Contents 📑

- [Features](#features)
- [How to Use](#how-to-use)
- [Technologies](#technologies)
- [Tests](#tests)
- [Requirements](#requirements)
- [Contributing](#contributing)

## Features

- 🔍 List all GitHub repositories for a given Github user (excluding forks)
- 🌿 Display branches available in each repository.
- ✅ Handle situations where a user does not exist on GitHub (returns 404 response).
- 📄 Data is presented in JSON format.

## How to Use

1. **Running the application:**

   To run the application locally, make sure you have JDK (Java Development Kit) version 21 or higher and Maven installed.

   Then, start the application using the following command:

   ``` mvn quarkus:dev ```

   or

   ``` ./mvnw quarkus:dev ```

2. **Using the API:**

   Once the application is running, access the API under the following URL:

   ```http
   http://localhost:8080/github/repos-with-branches/{user}
   ```
   or use the following endpoint to list repositories
   ```bash
   GET /github/repos-with-branches/{user}
   ```
   Replace **{user}** with the GitHub username whose repositories and branches you want to browse.

3. **JSON response:**

   Here’s an example of the response you will receive when calling the endpoint with a valid user:

   ```json
   [
     {
        "repo_name": "Hello-World",
        "owner_login": "octocat",
        "branches_in_repo": [
            {
                "branch_name": "master",
                "last_commit_sha": "7fd1a60b01f91b314f59955a4e4d4e80d8edf11d"
            },
            {
                "branch_name": "octocat-patch-1",
                "last_commit_sha": "b1b3f9723831141a31a1a7252a213e216ea76e56"
            },
            {
                "branch_name": "test",
                "last_commit_sha": "b3cbd5bbd7e81436d2eee04537ea2b4c0cad4cdf"
            }
        ]
     }
   ]
   ```  

4. **In case of a non-existent user:**

   If the GitHub user does not exist, the application will return a 404 response with the following format:

   ```json
   {
     "status": 404,
     "message": "User 'nonexistentUser' not found on GitHub"
   }
   ```

## Technologies
The application was built using the following technologies:

-  **Java 21** - The programming language used
-  **Quarkus 3** –  Java framework for building the API
-  **RESTEasy Reactive** – For handling HTTP requests and responses
- **RestAssured** – For integration testing
-  **GitHub API** – To fetch repository and branch data

## Tests
The application includes automated integration tests to ensure the API works correctly.

1. **To run the tests, use the following command:**

   ```bash mvn test```

   or

   ```bash ./mvnw test```

2. **Test Coverage**

   **Happy Path**: Tests the successful retrieval of repositories and branches for a valid user.  
   **Error Handling**: Tests the 404 response for a non-existing user.



## Requirements
- JDK 21
- Maven 3.8+
- GitHub API access (no token required for public repositories)

## Contributing
If you want to contribute to the development of the project, feel free to open Pull Requests. Any ideas for
improvements are welcome!