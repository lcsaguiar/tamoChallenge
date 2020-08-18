package com.tamo.calendar.dao;

import org.testcontainers.containers.PostgreSQLContainer;

public class PostgresSqlContainer extends PostgreSQLContainer<PostgresSqlContainer> {
        private static final String IMAGE_VERSION = "postgres";
        private static PostgresSqlContainer container;

        private PostgresSqlContainer() {
            super(IMAGE_VERSION);
        }

        public static PostgresSqlContainer getInstance() {
            if (container == null) {
                container = new PostgresSqlContainer();
            }
            return container;
        }

        @Override
        public void start() {
            super.start();
            System.setProperty("DB_URL", container.getJdbcUrl());
            System.setProperty("DB_USERNAME", container.getUsername());
            System.setProperty("DB_PASSWORD", container.getPassword());
        }

        @Override
        public void stop() {
            //do nothing, JVM handles shut down
        }
}
