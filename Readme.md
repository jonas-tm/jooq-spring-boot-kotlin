# Spring Boot JOOQ Example

This project demonstrates JOOQ usage with a Spring Boot application utilizing 
- Postgresql database
- Flyway database migration
- Docker

When build JOOQ files be automatically generated (more  [here](#Generate-JOOQ-DB-Files))

# Local Setup
Always run these things first. It will also run [JOOQ file generation](#Generate-JOOQ-DB-Files)
```
./gradlew build
```

# Starting Locally
Spring Docker plugin is added which will automatically run the compose file which also does the database migration.
```
./gradlew bootRun
```

# Generate JOOQ DB Files
The Gradle build file defines a custom job to automatically generate JOOQ files.
It does the following steps for this:
1. Start postgres DB container (`composeUp`)
2. Runs migrations scripts on the DB (`flywayMigrate`)
3. Runs jooq DSL generation with connection to DB (`generateJooq`)
4. Stops and removes the DB container (`composeDown`)

Before running make sure Docker is started and compose installed.
In case something fails (e.g. error in migration script) make sure to run `composeDown` before retrying.
```
./gradlew generateDatabaseTypes
```
