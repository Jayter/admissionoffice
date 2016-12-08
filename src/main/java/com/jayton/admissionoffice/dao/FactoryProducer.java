package com.jayton.admissionoffice.dao;

import com.jayton.admissionoffice.dao.jdbc.PostgresDaoFactory;

/**
 * Created by Jayton on 03.12.2016.
 */
public class FactoryProducer {
    private final PostgresDaoFactory postgresDaoFactory = new PostgresDaoFactory();

    private static final FactoryProducer instance = new FactoryProducer();

    private FactoryProducer() {
    }

    public static FactoryProducer getInstance() {
        return instance;
    }

    public synchronized PostgresDaoFactory getPostgresDaoFactory() {
        return postgresDaoFactory;
    }
}
