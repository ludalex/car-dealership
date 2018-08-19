Car Dealership
================================================
An example of a REST API for the `adverts` resource backed by a Scala CRUD backend made with:
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

Likewise, you can also send a **GET** to retrieve all saved Car Adverts with optional descending/ascending sorting by any field (by default it will sort by ID):

```bash
curl -X GET \
  'http://localhost:9000/adverts?sortBy=title:desc' 
```

You can also retrieve a single Car Advert by ID (**GET/:id**):

```bash
curl -X GET \
  http://localhost:9000/adverts/1
```

Or modify an existing one (**PUT/:id**):

```bash
curl -X PUT \
  http://localhost:9000/adverts/1 \
  -d '{
	"title": "BMW 118d",
	"fuel": "Diesel",
	"price": 20000000,
	"isNew": false,
	"mileage": 42,
	"firstRegistration": "2017-09-15"
}'
```

And of course delete a Car Advert (**DELETE/:id**)
```bash
curl -X DELETE \
  http://localhost:9000/adverts/1
```