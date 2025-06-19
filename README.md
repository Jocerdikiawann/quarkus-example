# Description

I'm using the [Product-Integration](https://github.com/Jocerdikiawann/product-integration) repo as the frontend for this app

# Step for run

1. create `.env` file.
2. put this code to `.env` :

```
PG_USER=admin
PG_PASSWORD=admin
DB_NAME=demo
PG_PORT=5432
ADM_MAIL=demo@mail.com
ADM_PASSWORD=admin
ADM_PORT=8000
JWT_ISSUER=fd5e5bdbff30f08444dccfacb18113e4d3dfcac8
APP_API_KEY=b78034aacf3559fffbfcb545d9a9122efb93181f
JWT_EXPIRATION=86400
QUARKUS_PORT=8080
QUARKUS_DATASOURCE_REACTIVE_URL=vertx-reactive:postgresql://postgres:${PG_PORT}/${DB_NAME}
QUARKUS_SWAGGER_UI_ALWAYS_INCLUDE=true
QUARKUS_HTTP_CORS_ENABLED=true
QUARKUS_HTTP_CORS_ORIGINS=/.*/
QUARKUS_HTTP_CORS_METHODS=GET,PUT,POST,DELETE
QUARKUS_HTTP_CORS_HEADERS=origin, accept, authorization, content-type, x-requested-with
QUARKUS_HTTP_CORS_ACCESS_CONTROL_ALLOW_CREDENTIALS=true
QUARKUS_TLS_TRUST_ALL=true
```

3. Run use docker compose `docker compose up -d`
