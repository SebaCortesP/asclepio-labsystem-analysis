# Imagen base con JDK para ejecutar tu app
FROM openjdk:21-ea-24-oracle
# Directorio de trabajo dentro del contenedor
WORKDIR /app
# Copia el archivo JAR generado al contenedor
COPY target/asclepio-0.0.1-SNAPSHOT.jar app.jar
# COPY build/libs/*.jar app.jar
COPY Wallet_X0KHXRVQD9T51XEF /opt/wallet
ENV TNS_ADMIN=/opt/wallet
# Expón el puerto que usa Spring Boot (por defecto es 8080)
EXPOSE 8090
# Comando para ejecutar tu aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]