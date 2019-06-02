package de.johanneswirth.tac.authservice;

import de.johanneswirth.tac.authservice.services.LoginService;
import de.johanneswirth.tac.authservice.services.PasswordChangeService;
import de.johanneswirth.tac.authservice.services.PasswordRecoverService;
import de.johanneswirth.tac.authservice.services.RegisterService;
import de.johanneswirth.tac.common.AuthenticationRequired;
import de.johanneswirth.tac.common.DBUtils;
import io.dropwizard.Application;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.discovery.DiscoveryBundle;
import io.dropwizard.discovery.DiscoveryFactory;
import io.dropwizard.jdbi3.JdbiFactory;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.jdbi.v3.core.Jdbi;

public class AuthServiceApp extends Application<AuthConfiguration> {

    private final DiscoveryBundle<AuthConfiguration> discoveryBundle = new DiscoveryBundle<AuthConfiguration>() {
        @Override
        public DiscoveryFactory getDiscoveryFactory(AuthConfiguration configuration) {
            return configuration.getDiscoveryFactory();
        }

    };

    public static void main(String[] args) throws Exception {
        new AuthServiceApp().run(args);
    }

    public void initialize(Bootstrap<AuthConfiguration> bootstrap) {
        bootstrap.setConfigurationSourceProvider(
                new SubstitutingSourceProvider(bootstrap.getConfigurationSourceProvider(),
                        new EnvironmentVariableSubstitutor(true)
                )
        );
        bootstrap.addBundle(new MigrationsBundle<AuthConfiguration>() {
            @Override
            public DataSourceFactory getDataSourceFactory(AuthConfiguration configuration) {
                return configuration.getDataSourceFactory();
            }
        });
        //bootstrap.addBundle(discoveryBundle);
    }

    @Override
    public void run(AuthConfiguration config, Environment environment) {
        PasswordUtils utils = new PasswordUtils(config.getPublicKey(), config.getPrivateKey());
        environment.lifecycle().manage(utils);
        final JdbiFactory factory = new JdbiFactory();
        final Jdbi jdbi = factory.build(environment, config.getDataSourceFactory(), "mysql");
        jdbi.registerRowMapper(new PairMapperFactory());

        //DBUtils.initDB(jdbi);
        //DBUtils.updateDB(jdbi, 0);
        environment.jersey().register(new LoginService(jdbi, utils));
        environment.jersey().register(new RegisterService(jdbi, utils));
        environment.jersey().register(new PasswordChangeService(jdbi, utils));
        environment.jersey().register(new PasswordRecoverService(jdbi, utils));
        environment.jersey().register(new AuthenticationRequired(config.getPublicKey()));
    }


}
