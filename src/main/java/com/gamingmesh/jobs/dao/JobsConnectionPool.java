package com.gamingmesh.jobs.dao;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.gamingmesh.jobs.Jobs;

public class JobsConnectionPool {

    private JobsConnection connection;
    private String url;
    private String username;
    private String password;
    private boolean postgreSql;

    public JobsConnectionPool(String driverName, String url, String username, String password) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
	this(driverName, url, username, password, false);
    }

    public JobsConnectionPool(String driverName, String url, String username, String password, boolean postgreSql) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
	Driver driver = (Driver) Class.forName(driverName, true, Jobs.getJobsClassloader()).newInstance();
	JobsDrivers jDriver = new JobsDrivers(driver);
	DriverManager.registerDriver(jDriver);
	this.url = url;
	this.username = username;
	this.password = password;
	this.postgreSql = postgreSql;
    }

    public synchronized JobsConnection getConnection() throws SQLException {
	if (this.connection != null && (this.connection.isClosed() || !this.connection.isValid(1))) {
	    try {
		this.connection.closeConnection();
	    } catch (SQLException e) {
	    }
	    this.connection = null;
	}
	if (this.connection == null) {
	    Connection conn = DriverManager.getConnection(this.url, this.username, this.password);
	    this.connection = new JobsConnection(conn, postgreSql);
	}
	return this.connection;
    }

    public synchronized void closeConnection() {
	if (this.connection != null)
	    try {
		this.connection.closeConnection();
	    } catch (SQLException e) {
		e.printStackTrace();
	    }
    }
}
