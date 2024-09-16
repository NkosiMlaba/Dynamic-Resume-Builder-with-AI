# Variables
WEBAPI_JAR_FILE = target/resume-builder-1.0-SNAPSHOT-webapi-jar-with-dependencies.jar

# Default target
.PHONY: all
all: clean build

# Clean the project
.PHONY: clean
clean:
	mvn clean

# Build the project and package it into a JAR file
.PHONY: build
build:
	mvn package

# Run tests
.PHONY: tests
tests:
	mvn test

# Target to bild and run webapi
.PHONY: webapi
webapi: build
	java -jar $(WEBAPI_JAR_FILE)

# Target to run webapi directly without building
.PHONY: run-webapi
run-webapi: 
	java -jar $(WEBAPI_JAR_FILE) 7000

# Target to automate pushing to github
.PHONY: push
push:
	@read -p "Enter commit message: " msg; \
	git add .; \
	git commit -m "$$msg"; \
	git push