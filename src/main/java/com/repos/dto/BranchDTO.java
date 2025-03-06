package com.repos.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record BranchDTO(
        @JsonProperty("branch_name") String branchName,
        @JsonProperty("last_commit_sha") String lastCommitSha
) {}

