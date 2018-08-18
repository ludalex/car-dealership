AutoScout24 Scala Test Task
================================================
A simple REST API for the `adverts` resource backed by a Scala CRUD application made with:
 - [Play 2.4](https://www.playframework.com/) 
 - [Slick](http://slick.lightbend.com/doc/3.1.1/) 
 - [Play-Slick](https://www.playframework.com/documentation/latest/PlaySlick) 
 - H2 in-memory/file database (for test purposes, easily swappable to other relational DB engines (i.e MySQL, Postgres) thanks to Slick).



More Information
================

- A [Postman](https://www.getpostman.com/) collection useful to quickly test the API is available in the root folder [here](Car Adverts API.postman_collection.json) 
  
  
Usage
================
### Running

You need to download and install sbt for this application to run.

Once you have sbt installed, the following at the command prompt will start up Play in development mode:

```bash
sbt run
```

Play will start up on the HTTP port at <http://localhost:9000/>.   You don't need to deploy or reload anything -- changing any source code while the server is running will automatically recompile and hot-reload the application on the next HTTP request. 

### Usage

If you call the same URL from the command line, you’ll see JSON. Using httpie, we can execute the command:

```bash
http --verbose http://localhost:9000/adverts
```

and get back:

```routes
GET /adverts HTTP/1.1
```

Likewise, you can also send a POST directly as JSON:

```bash
http --verbose POST http://localhost:9000/adverts title="BMW M4" fuel="Gasoline" price:=60000000 isNew:=true
```

and get:

```routes
POST /v1/posts HTTP/1.1
```