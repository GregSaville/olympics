import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context
import java.sql.Statement

open class BaseMigration : BaseJavaMigration() {

    lateinit var db: Statement

    override fun migrate(context: Context) {
        db = context.connection.createStatement()
        // Call an internal function to execute the migration logic
        internalMigration {
            migrateStatements()
        }
    }

    // This function will be overridden by subclasses to define specific migration logic
    open fun migrateStatements() {
        throw NotImplementedError("Subclasses must implement migrateStatements()")
    }

    // Inline function to execute the migration logic
    private inline fun internalMigration(migration: () -> Unit) {
        migration()
        db.close()
    }
}
