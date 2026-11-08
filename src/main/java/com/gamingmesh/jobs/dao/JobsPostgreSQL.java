package com.gamingmesh.jobs.dao;

import com.gamingmesh.jobs.Jobs;
import com.gamingmesh.jobs.dao.JobsManager.DataBaseType;

/** PostgreSQL backend using the same DAO operations as the MySQL backend. */
public class JobsPostgreSQL extends JobsMySQL {

    private static final String DRIVER = "org.postgresql.Driver";

    JobsPostgreSQL(Jobs plugin, String hostname, String database, String username, String password,
            String prefix, String schema, boolean ssl) {
        super(plugin, DRIVER, buildUrl(hostname, database, schema, ssl), username, password, prefix, true);
        setDbType(DataBaseType.PostgreSQL);
    }

    private static String buildUrl(String hostname, String database, String schema, boolean ssl) {
        String safeSchema = schema == null || schema.trim().isEmpty() ? "public" : schema.trim();
        return "jdbc:postgresql://" + hostname + "/" + database
                + "?currentSchema=" + safeSchema + "&ssl=" + ssl;
    }

    public void initialize() {
        setUp();
    }
}
