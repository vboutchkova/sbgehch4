package com.geh.datasource;

import java.util.Collections;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

/**
 * Bean for DB setup with MongoDB and Morphia
 * 
 * @author Vera Boutchkova
 */
public class MongoDataSource {

	private String dbName;
	private String userName;
	private String password;
	private String host;
	private int port;

	private Datastore datastore;
	private MongoClient mongoClient;

	public Datastore getDatastore() {
		synchronized (this) {
			if (datastore == null) {

				MongoCredential credentials = MongoCredential.createCredential(userName, dbName,
						password.toCharArray());
				ServerAddress server = new ServerAddress(host, port);

				mongoClient = new MongoClient(server, Collections.singletonList(credentials),
						new MongoClientOptions.Builder().build());
				Morphia morphia = new Morphia();
				morphia.mapPackage("com.geh.mongodb.morphia.entities");

				this.datastore = morphia.createDatastore(mongoClient, dbName);
				this.datastore.ensureIndexes();
			}
		}

		return datastore;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public void setPort(int port) {
		this.port = port;
	}

}