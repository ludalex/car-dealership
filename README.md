AutoScout24 Scala Test Task
================================================
A simple REST API for the `adverts` resource backed by a Scala CRUD application made with:
 - [Play 2.4](https://www.playframework.com/) 
 - [Slick](http://slick.lightbend.com/doc/3.1.1/) 
 - [Play-Slick](https://www.playframework.com/documentation/latest/PlaySlick) 
 - [H2](http://www.h2database.com/html/main.html) in-memory/file database (for test purposes, easily swappable to other relational DB engines (i.e MySQL, Postgres) thanks to Slick).



More Information
================

- Swagger UI is available at <http://localhost:9000/docs/> after the application starts to evaluate and test all `advert` API endpoints.
- A [Postman](https://www.getpostman.com/) collection useful to browse/test the API is also available in the root folder [here](Car_Adverts_API.postman_collection.json) 
- Project uses [Evolutions](https://www.playframework.com/documentation/2.4.x/Evolutions) to perform database schema migrations. The initial migration to setup the base schema will run at application startup, no configuration work is needed.
- CORS is fully enabled on all endpoints.
  
Usage
================
### Running

You need to download and install sbt for this application to run.

Once you have sbt installed, the following at the command prompt will start up the application:

```bash
sbt run
```

Application will start up on the HTTP port at <http://localhost:9000/>.  

You can run tests with: 
```bash
sbt test
```

### Sample Usage

Using curl, we can execute the command to **POST** a new Car Advert:

```bash
curl -X POST \
  http://localhost:9000/adverts \
  -H 'Content-Type: application/json' \
  -d '{
	"title": "BMW M4",
	"fuel": "Gasoline",
	"price": 60000000,
	"isNew": false,
	"mileage": 1337,
	"firstRegistration": "2017-09-15"
}'
```

Likewise, you can also send a **GET** to retrieve all saved Car Adverts:

```bash
curl -X GET \
  'http://localhost:9000/adverts?sortBy=title:desc' 
```

Notes
================
To the developer(s) evaluating this project: even though I have a Java background, please let me remind you that this was the very first chance for me to Play around (pardon the pun) with the world of Scala and Play! Please forgive any obvious mistakes :) 