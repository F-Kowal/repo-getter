package com.repos;

import java.util.LinkedHashMap;

import com.repos.model.Repository;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.List;
import java.util.Map;

@Path("/github")
public class GithubResource {

    @Inject
    @RestClient
    GithubClient githubClient;

    @GET
    @Path("/repos-with-branches/{user}")
    @Produces(MediaType.APPLICATION_JSON)
    public Multi<Map<String, Object>> getReposWithBranches(@PathParam("user") String user) {

        return githubClient.getRepositories(user)
                .onItem().transformToMulti(repos -> Multi.createFrom().iterable(repos))
                .filter(repo -> !repo.fork)
                .flatMap(repo -> githubClient.getBranches(user, repo.name)
                        .onItem().transform(branches -> {
                            Map<String, Object> resultMap = new LinkedHashMap<>();
                            resultMap.put("repo_name", repo.name);
                            resultMap.put("owner_login", repo.owner.login);
                            resultMap.put("branches_in_repo", branches.stream()
                                    .map(branch -> {
                                        Map<String, Object> branchMap = new LinkedHashMap<>();
                                        branchMap.put("branch_name", branch.name);
                                        branchMap.put("last_commit_sha", branch.commit.sha);
                                        return branchMap;
                                    })
                                    .toList());

                            return resultMap;
                        })
                        .toMulti()
                );
    }
}
