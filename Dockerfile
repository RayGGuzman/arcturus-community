
# Build stage
FROM maven:3.9-eclipse-temurin-8 AS builder

WORKDIR /app

# Copy Maven project
COPY . .

# Compile the emulator
RUN mvn -B package \
    && JAR_FILE="$(find target -maxdepth 1 -type f -name 'Habbo*-with-dependencies.jar' -print -quit)" \
    && test -n "$JAR_FILE" \
    && cp "$JAR_FILE" /app/Habbo.jar

# Runtime stage
FROM eclipse-temurin:8-jre

WORKDIR /

# Copy compiled emulator
COPY --from=builder /app/Habbo.jar /Habbo.jar

# Start emulator
ENTRYPOINT ["java", "-Dfile.encoding=UTF-8", "-jar", "/Habbo.jar"]
