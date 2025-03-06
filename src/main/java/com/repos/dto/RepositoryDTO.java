package com.repos.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record RepositoryDTO(
        @JsonProperty("repo_name") String repoName,
        @JsonProperty("owner_login") String ownerLogin,
        @JsonProperty("branches_in_repo") List<BranchDTO> branches
) {}
