package com.repos;

import java.util.LinkedHashMap;
import java.util.List;

import com.repos.model.Repository;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import java.util.stream.Collectors;
import java.util.Map;

@Path("/github")
public class GithubResource {

    @Inject
    @RestClient
    GithubClient githubClient;

    @GET
    @Path("/repos-with-branches/{user}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<List<Map<String, Object>>> getReposWithBranches(@PathParam("user") String user) {

        return githubClient.getRepositories(user)
                .onItem().transformToUni(repos -> {

                    List<Repository> nonForkRepos = repos.stream()
                            .filter(repo -> !repo.fork)
                            .toList();

                    List<Uni<Map<String, Object>>> reposWithBranches = nonForkRepos.stream()
                            .map(repo -> githubClient.getBranches(user, repo.name)
                                    .onItem().transform(branches -> {
                                        Map<String, Object> resultMap = new LinkedHashMap<>();
                                        resultMap.put("reponame", repo.name);
                                        resultMap.put("ownerlogin", repo.owner.login);
                                        resultMap.put("branchesinrepo", branches.stream()
                                                .map(branch -> {
                                                    Map<String, Object> branchMap = new LinkedHashMap<>();
                                                    branchMap.put("name", branch.name);
                                                    branchMap.put("sha", branch.commit.sha);
                                                    return branchMap;
                                                })
                                                .collect(Collectors.toList()));

                                        return resultMap;
                                    })
                            )
                            .collect(Collectors.toList());

                    return Uni.combine().all().unis(reposWithBranches)
                            .combinedWith(results -> results.stream()
                                    .map(result -> (Map<String, Object>) result)
                                    .collect(Collectors.toList()));
                });
    }
}
