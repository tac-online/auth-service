package de.johanneswirth.tac.authservice;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class AuthConfiguration extends Configuration {

    @NotEmpty
    private String publicKey;
    @NotEmpty
    private String privateKey;

    @JsonProperty
    public String getPublicKey() {
        return publicKey;
    }

    @JsonProperty
    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    @JsonProperty
    public String getPrivateKey() {
        return privateKey;
    }

    @JsonProperty
    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    @Valid
    @NotNull
    private DataSourceFactory database = new DataSourceFactory();

    @JsonProperty("database")
    public void setDataSourceFactory(DataSourceFactory factory) {
        this.database = factory;
    }

    @JsonProperty("database")
    public DataSourceFactory getDataSourceFactory() {
        return database;
    }
}