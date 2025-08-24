package com.university.ManageNotes.dto.Request;

import lombok.Setter;

public class DbSyncRequest {
    private String jdbcUrl;
    @Setter
    private String username;
    @Setter
    private String password;
    private String query;

    // getters and setters
    public String getJdbcUrl() {return jdbcUrl;}
    public String getUsername() {return username;}

    public String getPassword() {return password;}

    public String getQuery() {return query;}
}
