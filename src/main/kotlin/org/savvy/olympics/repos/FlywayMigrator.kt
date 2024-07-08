package org.savvy.olympics.repos

import org.flywaydb.core.Flyway
import org.savvy.olympics.db.migration.V1__CreateUserMigration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource


//@Configuration
//class FlywayMigrator {
//
//    @Autowired
//    lateinit var dataSource: DataSource
//
//    @Bean(initMethod = "migrate")
//    fun flyway(): Flyway {
//        return Flyway.configure()
//            .dataSource(dataSource)
//            .javaMigrations(V1__CreateUserMigration())
//            .load()
//    }
//
//    @Bean
//    fun flywayMigrationStrategy(): FlywayMigrationStrategy {
//        return FlywayMigrationStrategy { flyway ->
//            flyway.repair()
//            flyway.migrate()
//        }
//    }
//}


