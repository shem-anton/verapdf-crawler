package org.verapdf.service;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import java.io.IOException;

public class VerapdfServiceApp extends Application<VeraPDFServiceConfiguration> {

    public static void main(String[] args) throws Exception {
        new VerapdfServiceApp().run(args);
    }

    @Override
    public String getName() {
        return "VeraPDF Service";
    }

    @Override
    public void initialize(Bootstrap<VeraPDFServiceConfiguration> bootstrap) { }

    @Override
    public void run(VeraPDFServiceConfiguration configuration, Environment environment) throws IOException {
        environment.jersey().setUrlPattern("/*");
        environment.jersey().register(new ValidationResource(configuration.getVerapdfPath(), configuration.getLogiusUrl()));
    }
}
