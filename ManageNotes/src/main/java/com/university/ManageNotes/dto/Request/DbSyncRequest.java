package com.university.ManageNotes.dto.Request;

public class DbSyncRequest {
    private String jdbcUrl;
    private String username;
    private String password;
    private String query;

    // getters and setters
    public String getJdbcUrl() {return jdbcUrl;}
    public void setJdbcUrl(String jdbcUrl) {this.jdbcUrl = jdbcUrl;}
    public String getUsername() {return username;}
    public void setUsername(String username) {this.username = username;}
    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}
    public String getQuery() {return query;}
    public void setQuery(String query) {this.query = query;}
}
