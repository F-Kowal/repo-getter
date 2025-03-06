package com.repos;

import com.repos.dto.RepositoryDTO;
import com.repos.dto.BranchDTO;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;


@Path("/github")
public class GithubResource {

    @Inject
    @RestClient
    GithubClient githubClient;

    @GET
    @Path("/repos-with-branches/{user}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> getReposWithBranches(@PathParam("user") String user) {
        return githubClient.getRepositories(user)
                .onItem().transformToMulti(repos -> Multi.createFrom().iterable(repos))
                .filter(repo -> !repo.fork())
                .flatMap(repo -> githubClient.getBranches(user, repo.name())
                        .onItem().transform(branches ->
                                new RepositoryDTO(
                                        repo.name(),
                                        repo.owner().login(),
                                        branches.stream()
                                                .map(branch -> new BranchDTO(branch.name(), branch.commit().sha()))
                                                .toList()
                                )
                        ).toMulti()
                )
                .collect().asList()
                .onItem().transform(repoList -> Response.ok(repoList).build());
    }
}
