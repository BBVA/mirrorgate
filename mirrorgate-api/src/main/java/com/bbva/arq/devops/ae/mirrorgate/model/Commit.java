package com.bbva.arq.devops.ae.mirrorgate.model;

import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Document(collection = "commits")
@CompoundIndex(name = "stats", def = "{'repository' :  1, 'timestamp': 1}")
public class Commit extends BaseIdModel {

    private String hash;
    private String repository;
    private Integer timestamp;
    private String message;
    private String authorName;
    private String authorEmail;
    private String committerName;
    private String committerEmail;
    private List<String> parentsIds;
    private Map<String, Integer> branches;

    public String getHash() {
        return hash;
    }

    public Commit setHash(String hash) {
        this.hash = hash;
        return this;
    }

    public Integer getTimestamp() {
        return timestamp;
    }

    public Commit setTimestamp(Integer timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public Commit setMessage(String message) {
        this.message = message;
        return this;
    }

    public String getAuthorName() {
        return authorName;
    }

    public Commit setAuthorName(String authorName) {
        this.authorName = authorName;
        return this;
    }

    public String getAuthorEmail() {
        return authorEmail;
    }

    public Commit setAuthorEmail(String authorEmail) {
        this.authorEmail = authorEmail;
        return this;
    }

    public String getCommitterName() {
        return committerName;
    }

    public Commit setCommitterName(String committerName) {
        this.committerName = committerName;
        return this;
    }

    public String getCommitterEmail() {
        return committerEmail;
    }

    public Commit setCommitterEmail(String committerEmail) {
        this.committerEmail = committerEmail;
        return this;
    }

    public List<String> getParentsIds() {
        return parentsIds;
    }

    public Commit setParentsIds(List<String> parentsIds) {
        this.parentsIds = parentsIds;
        return this;
    }

    public String getRepository() {
        return repository;
    }

    public Commit setRepository(String repository) {
        this.repository = repository;
        return this;
    }

    public Map<String, Integer> getBranches() {
        return branches;
    }

    public Commit setBranches(Map<String, Integer> branches) {
        this.branches = branches;
        return this;
    }
}
