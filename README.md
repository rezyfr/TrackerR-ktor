[![Kotlin](https://img.shields.io/badge/kotlin-1.9.0-blue.svg?logo=kotlin)](http://kotlinlang.org)
[![Ktor](https://img.shields.io/badge/ktor-2.3.3-blue.svg)](https://github.com/ktorio/ktor)
[![Build](https://github.com/rezyfr/TrackerR-ktor/workflows/Build/badge.svg)](https://github.com/rezyfr/TrackerR-ktor/actions/workflows/build.yml)
[![codecov](https://codecov.io/gh/rezyfr/TrackerR-ktor/branch/master/graph/badge.svg?token=v2k9oObm0C)](https://codecov.io/gh/rezyfr/TrackerR-ktor)

# A simple expense tracker API written in Kotlin using Ktor

1. Clone this repository
2. Run `./gradlew run` to start the server
3. Open `http://localhost:8080` in your browser

## Libraries
- [Ktor](https://github.com/ktorio/ktor) - Kotlin async web framework
- [Kodein](https://github.com/kosi-libs/Kodein) - Dependency injection
- [PostgreSQL](https://www.postgresql.org/) - Database
- [Exposed](https://github.com/JetBrains/Exposed) - Kotlin SQL framework
- [H2](https://github.com/h2database/h2database) - Embeddable database
- [HikariCP](https://github.com/brettwooldridge/HikariCP) - High performance JDBC connection pooling
- [Flyway](https://flywaydb.org/) - Database migrations
- [JUnit 5](https://junit.org/junit5/) and [AssertJ](http://joel-costigliola.github.io/assertj/)
  for testing
- [Kover](https://github.com/Kotlin/kotlinx-kover) for code coverage, publishing
  to [Codecov](https://about.codecov.io/) through GitHub Actions
- [Tembo](https://tembo.io/) for PostgreSQL hosting
- [Fly.io](https://fly.io/) for API hosting

## API Documentation

### User Authentication
`POST /v1/user/register` - Register a new user
`POST /v1/user/login` - Login user
`POST /v1/user/refresh-token` - Refresh authentication token
`GET /v1/protected/user/check-token` - Check token validity

### Wallet Management
`POST /v1/protected/wallet/create` - Create a new wallet
`GET /v1/protected/wallet` - Get all wallets
`POST /v1/protected/wallet/update/balance` - Update wallet balance
`GET /v1/protected/wallet/balance` - Get wallet balance

### Category Management
`POST /v1/protected/category` - Add a new category
`GET /v1/protected/category` - Get all categories

### Icon Management
`GET /v1/icon` - Get icons
`POST /v1/icon/create` - Add a new icon

### Transaction Management
`POST /v1/protected/transaction` - Create a new transaction
`GET /v1/protected/transaction/recent` - Get recent transactions
`POST /v1/protected/transaction/summary` - Get monthly summary
`GET /v1/protected/transaction/frequency` - Get transaction frequency
`GET /v1/protected/transaction/with-date` - Get transactions with date
`GET /v1/protected/transaction/report` - Get transaction report

## Structure
      + db/
          Database table definition and initialization
      + repository/
          Define the databases operations
      + service/
          Logic layer and transformation data
      + config 
          Application configuration and modules definition
      + utils/
          JWT and password encrytpion
      + controller/
          Controller will fetch/write data through API objects and unhappy paths will be handled throwing exceptions.
      - Application.kt <- The main class

