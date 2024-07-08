import org.jetbrains.kotlin.gradle.dsl.JvmTarget


plugins {
	id("org.springframework.boot") version "3.3.1"
	id("io.spring.dependency-management") version "1.1.5"
	kotlin("jvm") version "2.0.0"
	kotlin("plugin.spring") version "1.9.24"
	kotlin("plugin.jpa") version "1.9.24"
}

group = "org.savvy"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_22
	targetCompatibility = JavaVersion.VERSION_22
}

kotlin {
	compilerOptions {
		jvmTarget = JvmTarget.JVM_22
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web") // Use Web starter for REST APIs
	implementation("org.springframework.boot:spring-boot-starter-data-jpa") // Use JPA for data access if needed
	implementation("org.postgresql:postgresql") // Example database driver
	implementation("org.jetbrains.kotlin:kotlin-stdlib")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin") // JSON serialization/deserialization
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test")
}

tasks.test {
	useJUnitPlatform()
}
