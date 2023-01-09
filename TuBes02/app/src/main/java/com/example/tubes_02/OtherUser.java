package com.example.tubes_02;

import java.util.ArrayList;
import java.util.Arrays;

public class OtherUser {
    public static final String ROLE_STUDENT = "student";
    public static final String ROLE_LECTURER = "lecturer";
    public static final String ROLE_ADMIN = "admin";
    private String id;
    private String name;
    private String email;
    private boolean roles[]; //Index 0 = admin, 1 = lecturer, 2 = student

    public OtherUser(String id, String name, String email, String[] roles){
        this.id = id;
        this.name = name;
        this.email = email;
        this.assignRoles(roles);
    }

    public OtherUser(String id, String name, String email, ArrayList<String> roles){
        this.id = id;
        this.name = name;
        this.email = email;
        this.assignRoles(roles);
    }

    private void assignRoles(String[] roles){
        this.roles = new boolean[3];
        for (int i = 0; i < roles.length; i++) {
            switch (roles[i]){
                case ROLE_STUDENT:
                    this.roles[2] = true;
                    break;
                case ROLE_LECTURER:
                    this.roles[1] = true;
                    break;
                case ROLE_ADMIN:
                    this.roles[0] = true;
                    break;
            }
        }
    }

    private void assignRoles(ArrayList<String> roles){
        this.roles = new boolean[3];
        for (int i = 0; i < roles.size(); i++) {
            switch (roles.get(i)){
                case ROLE_STUDENT:
                    this.roles[2] = true;
                    break;
                case ROLE_LECTURER:
                    this.roles[1] = true;
                    break;
                case ROLE_ADMIN:
                    this.roles[0] = true;
                    break;
            }
        }
    }

    @Override
    public String toString() {
        return name;
    }

    public String getName(){
        return this.name;
    }

    public String getId(){
        return this.id;
    }
}
