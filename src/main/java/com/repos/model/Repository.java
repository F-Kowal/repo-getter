package com.repos.model;

public class Repository {
    public String name;
    public Owner owner;
    public boolean fork;

    public static class Owner {
        public String login;
    }
}
