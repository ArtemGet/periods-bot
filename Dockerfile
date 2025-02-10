FROM maven:3.9.9

WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests
CMD java -jar target/app.jar
