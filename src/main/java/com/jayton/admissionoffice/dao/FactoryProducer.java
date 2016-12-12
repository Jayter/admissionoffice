package com.jayton.admissionoffice.dao;

import com.jayton.admissionoffice.dao.jdbc.PostgresDaoFactory;

public class FactoryProducer {
    private final PostgresDaoFactory postgresDaoFactory = new PostgresDaoFactory();

    private static final FactoryProducer instance = new FactoryProducer();

    private FactoryProducer() {
    }

    public static FactoryProducer getInstance() {
        return instance;
    }

    public PostgresDaoFactory getPostgresDaoFactory() {
        return postgresDaoFactory;
    }
}