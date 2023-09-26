import nu.studer.gradle.jooq.JooqEdition
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("nu.studer.jooq") version "8.2"
	id("org.springframework.boot") version "3.1.4"
	id("io.spring.dependency-management") version "1.1.3"
	id("org.graalvm.buildtools.native") version "0.9.27"
	id("org.flywaydb.flyway") version "9.22.1" // update in compose.yaml as well
	id("com.avast.gradle.docker-compose") version "0.17.5"
	kotlin("jvm") version "1.9.0"
	kotlin("plugin.spring") version "1.9.0"
}

group = "me.jonastm"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-jooq")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	developmentOnly("org.springframework.boot:spring-boot-docker-compose")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
	runtimeOnly("org.postgresql:postgresql")
	jooqGenerator("org.postgresql:postgresql")
}

dockerCompose {
	useComposeFiles = listOf("compose.yaml")
	projectNamePrefix = "jooq-sample"
}


flyway {
	url = "jdbc:postgresql://localhost:5432/postgres"
	user = "postgres"
	password = "postgres"
	locations = arrayOf("filesystem:db/migration")
}

jooq {
	// use jOOQ version defined in Spring Boot
	version = dependencyManagement.importedProperties["jooq.version"]
	edition = JooqEdition.OSS

	configurations {
		create("main") {  // name of the jOOQ configuration
			generateSchemaSourceOnCompilation.set(true)  // default (can be omitted)

			jooqConfiguration.apply {
//				logging = Logging.WARN
				jdbc.apply {
					driver = "org.postgresql.Driver"
					url = "jdbc:postgresql://localhost:5432/postgres"
					user = "postgres"
					password = "postgres"
				}
				generator.apply {
					name = "org.jooq.codegen.KotlinGenerator" // Generate Kotlin classes "org.jooq.codegen.DefaultGenerator"
					database.apply {
						name = "org.jooq.meta.postgres.PostgresDatabase"
						inputSchema = "public"
					}
					generate.apply {
						isDeprecated = false
						isRecords = true
						isImmutablePojos = true
						isFluentSetters = true
					}
					target.apply {
						packageName = "me.jonastm.jooqsample.adapter.out.persistence.entities"
						directory = "build/generated-src/jooq/main"  // default (can be omitted)
					}
					strategy.name = "org.jooq.codegen.DefaultGeneratorStrategy"
				}
			}
		}
	}
}


tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs += "-Xjsr305=strict"
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.named<nu.studer.gradle.jooq.JooqGenerate>("generateJooq") {
//	// declare Flyway migration scripts as inputs on the jOOQ task
//	inputs.files(fileTree("db/migration"))
//		.withPropertyName("migrations")
//		.withPathSensitivity(PathSensitivity.RELATIVE)

	// make jOOQ task participate in incremental builds (and build caching)
	allInputsDeclared = true
}

tasks.register("generateDatabaseTypes") {
	dependsOn("composeUp")

	// You can optionally run the migration as gradle task
	//	dependsOn("flywayMigrate")
	//	tasks.findByName("flywayMigrate")?.shouldRunAfter("composeUp")

	dependsOn("generateJooq")
	tasks.findByName("generateJooq")?.shouldRunAfter("composeUp")

	dependsOn("composeDown")
	tasks.findByName("composeDown")?.shouldRunAfter("generateJooq")
}

tasks.named("build") {
	dependsOn("generateDatabaseTypes")
}
