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
	implementation("com.twilio.sdk:twilio:8.31.1")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.postgresql:postgresql:42.6.0")
	implementation("org.liquibase:liquibase-core")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.slf4j:slf4j-api:2.0.9")
	implementation("ch.qos.logback:logback-classic:1.4.11")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test")
}


tasks.test {
	useJUnitPlatform()
}
