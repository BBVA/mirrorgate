package com.bbva.arq.devops.ae.mirrorgate.model;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.LinkedList;

@Document(collection = "commits")
public class Commit {

    private String _id;

    @Indexed
    private String hash;

    private Integer timestamp;

    @Indexed
    private String branchName;

    private String message;
    private String authorName;
    private String authorEmail;
    private String committerName;
    private String committerEmail;
    private LinkedList<String> parentsIds;

    @Indexed
    private String repository;

    public String getId() {
        return _id;
    }

    public Commit setId(String _id) {
        this._id = _id;
        return this;
    }

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

    public LinkedList<String> getParentsIds() {
        return parentsIds;
    }

    public Commit setParentsIds(LinkedList<String> parentsIds) {
        this.parentsIds = parentsIds;
        return this;
    }

    public String getBranchName() {
        return branchName;
    }

    public Commit setBranchName(String branchName) {
        this.branchName = branchName;
        return this;
    }

    public String getRepository() {
        return repository;
    }

    public Commit setRepository(String repository) {
        this.repository = repository;
        return this;
    }
}
