import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.4.5"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	war
	kotlin("jvm") version "1.4.32"
	kotlin("plugin.spring") version "1.4.32"
}

group = "ru.dm_ushakov"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
	mavenCentral()
}

val camelVersion = "3.9.0"

dependencies {
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.apache.camel.springboot:camel-spring-boot-starter:$camelVersion")
	implementation("org.apache.camel.springboot:camel-openapi-java-starter:$camelVersion")
	implementation("org.apache.camel.springboot:camel-undertow-starter:$camelVersion")
	implementation("org.apache.camel.springboot:camel-jackson-starter:$camelVersion")
	implementation("org.apache.camel.springboot:camel-mvel-starter:$camelVersion")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "11"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
