package com.repos;

import java.util.List;

import com.repos.model.Branch;
import com.repos.model.Repository;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.*;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;


@RegisterRestClient(configKey = "github-api")
public interface GithubClient {

    @GET
    @Path("/users/{user}/repos")
    Uni<List<Repository>> getRepositories(@PathParam("user") String user);

    @GET
    @Path("/repos/{user}/{repo}/branches")
    Uni<List<Branch>> getBranches(@PathParam("user") String user, @PathParam("repo") String repo);
}

