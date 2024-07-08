package org.savvy.olympics.db.migration

import BaseMigration

class V1__CreateUserMigration : BaseMigration() {
    override fun migrateStatements() {
        run {
            db.execute(
                """
            CREATE TABLE users (
                id UUID PRIMARY KEY,
                user_name VARCHAR(255) NOT NULL,
                phone_number VARCHAR(20),
                registration_date TIMESTAMP NOT NULL
            )
        """.trimIndent()
            )
        }
    }
}