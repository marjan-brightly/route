# Installation instruction

* Import project into Intellij as maven project
* Run compile maven task to build project
* Start RouteApplication run configuration
* Invoke application on http://localhost:8080/routing/{origin}/{destination}

Precondition is to have java 17 installed on local machine.

# Test cases

| Request                               | Response                                  |
|---------------------------------------|-------------------------------------------|
| http://localhost:8080/routing/CZE/ITA | {"route":["CZE","AUT","ITA"]}             |
| http://localhost:8080/routing/SRB/FRA | {"route":["SRB","HUN","AUT","CHE","FRA"]} |
| http://localhost:8080/routing/GBR/FRA | 404                                       |
| http://localhost:8080/routing/GBR/IRL | {"route":["GBR","IRL"]}                   |
| http://localhost:8080/routing/DDD     | 404                                       |