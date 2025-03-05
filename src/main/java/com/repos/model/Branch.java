package com.repos.model;

public class Branch {
    public String name;
    public Commit commit;

    public static class Commit {
        public String sha;
    }
}
