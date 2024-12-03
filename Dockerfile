FROM nuruldhamar/java17-node-16-jhipster:1.0.1


WORKDIR /app
RUN mkdir loggers
COPY ./bits-hr-payroll-0.0.1-SNAPSHOT.jar /app

COPY ./application-prod.yml /app
COPY ./entrypoint.sh /app


RUN chmod +x /app/entrypoint.sh

# Set the entrypoint
ENTRYPOINT ["/app/entrypoint.sh"]

