package org.vaadin.pontus.shoppinglist;

import java.net.MalformedURLException;

import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbConnector;
import org.ektorp.impl.StdCouchDbInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
public class ApplicationInitializer extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(ApplicationInitializer.class, args);
    }

    @Configuration
    @PropertySource(value = { "classpath:application.properties" })
    @ComponentScan({ "org.vaadin.pontus.shoppinglist.rest",
        "org.vaadin.pontus.shoppinglist.service",
        "org.vaadin.pontus.shoppinglist.repositories",
    "org.vaadin.pontus.shoppinglist.flow" })
    public static class MyConfiguration {

        @Autowired
        private Environment environment;

        @Bean
        public CouchDbInstance couchDbInstance() {
            HttpClient httpClient = null;
            try {
                httpClient = new StdHttpClient.Builder()
                        .url(environment
                                .getProperty("org.pontus.shoppinglist.couchdb"))
                        .build();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            return new StdCouchDbInstance(httpClient);
        }

        @Bean
        @Autowired
        public CouchDbConnector getPushSubscriptionConnector(
                CouchDbInstance couchDb) {
            return new StdCouchDbConnector("push", couchDb);
        }

        @Bean
        public WebMvcConfigurer corsConfigurer() {
            return new WebMvcConfigurerAdapter() {
                @Override
                public void addCorsMappings(CorsRegistry registry) {
                    registry.addMapping("/**").allowedOrigins(
                            environment.getProperty("http://localhost:8081"));
                }
            };
        }

    }

    @EnableWebSecurity
    static class WebSecConf extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            //@formatter:off
            http.cors().and().csrf().disable().authorizeRequests().antMatchers("/**").permitAll().and()
            .sessionManagement().sessionFixation().newSession();

            //@formatter:on
        }

        @Override
        public void configure(WebSecurity web) throws Exception {
            web.ignoring().antMatchers("/frontend/**", "/src/**", "/push_sw.js",
                    "/service.js", "/manifest.json", "/push.js", "/index.html");

            // Static resources are
            // ignored
        }
    }
}
