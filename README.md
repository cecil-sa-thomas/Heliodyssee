# Heliodysse

En cours de rédaction****

```powershell
$env:MYSQL_PASSWORD = "mdpgenerique"
```
```powershell
[Environment]::SetEnvironmentVariable("MYSQL_PASSWORD", "mdpgenerique", "User")
```
Pensez à redemarer l'ide oula fenetre powershell après execution

```powershell
echo $env:MYSQL_PASSWORD  
```


## Architecture du projet

```
Heliodysse
└── src
    └── main
        └── java
            └── fr
                └── thomascecil
                    └── heliodysse
                        ├── adapter
                        │   ├── in
                        │   │   ├── controller
                        │   └── out
                        │       ├── jpaEntity
                        │       ├── mapper
                        │       ├── mongoEntity
                        │       ├── repoImpl
                        │       ├── repository
                        ├── application
                        │   └── service
                        │       └── UserApplicationService.java
                        ├── domain
                        │   ├── model
                        │   │   ├── entity
                        │   │   └── enums
                        │   ├── port
                        │   │   ├── in
                        │   │   └── out
                        │   └── service
                        │       ├── payment
                        │       ├── BookingService.java
                        │       ├── FlightService.java
                        │       ├── PlanetService.java
                        │       ├── SpacePortService.java
                        │       └── UserService.java
                        └── ServletInitializer.java
```

