# OORI - An O-MI/O-DF RDF Integration Server

OORI is server that is able to monitor the values of O-DF objects on multiple O-MI nodes. To do this, it makes use of O-MI's subscription functionality. The O-DF structure is converted into RDF format using the [O-DF ontology](src/main/resources/ODF-Ontology.ttl) and stored to a SPARQL endpoint. As a result, the current values of a subscribed object are at any time available for querying in the SPARQL endpoint.

## Project Status

The project is currently in early alpha state. It has not yet been extensively tested and automated tests are missing.

## Contributions

The project has two main contributions:

1. A server exposing HTTP methods that allow to register O-DF structures that should be constantly converted and stored to an RDF triple store.   
2. An RDF ontology that defines classes and properties to describe O-DF structures. It is used by the server to perform O-DF conversion into RDF.

## Getting Started

To start using the server, first obtain the source code by either cloning or downloadin the git repository somewhere to your local system. In these instructions we assume you have the source code available under your home directory in the path ```~/oori```.

### Building the project

Go to a command prompt and move to the ```~/oori``` directory.

```
mvn clean package 
```

This will give you an executable jar file in the directory ```~/oori/target```. If you have docker installed on your machine, you can also create a docker image from the source code by issuing the command

```
mvn clean package docker:build 
```

### Running the Server

To run the server, simply execute the generated jar file using the following command:

```
java -jar oori-0.0.1-SNAPSHOT.jar
```

The filename varies depending on the current version of the project. The command will start the OORI server at the default port 8080.

### Configuration

Currently you can customize the following properties:
 
 1. The port the server runs on, and
 2. The SPARQL endpoint that should be used for storing the generated RDF data.

If you start OORI using java, you can set these properties in the following form:

```
java -Dserver.port=SERVER_PORT -Dendpoint.url=ENDPOINT_URL -jar oori-0.0.1-SNAPSHOT.jar
```

When using docker, these properties can also be set as environment variables named ```SERVER_PORT``` and ```ENDPOINT_URL``` in the Dockerfile.

## Usage

OORI provides the REST endpoint

```
/nodes
```

which, when called using the HTTP ```GET``` method, returns a list of all registered O-MI nodes and O-DF structures.

To register a new O-DF structure that is available at an O-MI node at OORI, send an HTTP ```POST``` request to the ```/nodes``` method with the body being a JSON structure holding two values:

1. ```url``` specifies the O-MI node URL which serves the O-DF structure, and
2. ```subscriptionXMLRequest``` which is a one-time read O-MI request that specifies the object(s) and infoitem(s) that should be monitored.
 
An exempary JSON body of such a POST request may look like the following: 

```
{
  "url": "http://otaniemi3d.cs.hut.fi/omi/node/",
  "subscriptionXMLRequest": "<?xml version='1.0' encoding='UTF-8'?><omi:omiEnvelope xmlns:xs='http://www.w3.org/2001/XMLSchema-instance' xmlns:omi='omi.xsd' version='1.0' ttl='0'><omi:read msgformat='odf'><omi:msg><Objects xmlns='odf.xsd'><Object><id>KarysHouse</id><InfoItem name='Air Handling Unit'/></Object></Objects></omi:msg></omi:read></omi:omiEnvelope>"
}
```

## Built With

* [Maven](https://maven.apache.org/) - Dependency Management
* [Spring Boot](https://projects.spring.io/spring-boot/) - Application Framework
* [docker-maven-plugin](https://github.com/spotify/docker-maven-plugin) - Docker Image Creation

## Future Work

Our immediate next steps will be to

* Implement automated JUnit tests
* Provide a ```docker-compose.yml``` file involving [Fuseki](https://jena.apache.org/documentation/serving_data/) as a preconfigured RDF storage backend
* Implement support for unsubscribing O-DF structures
* Implement generation of O-MI write requests at SPARQL CONSTRUCT of a new RDF value

The final goal will be to use the RDF data generated by OORI as a basis for integrating multiple O-MI/O-DF capable devices by orchestrate them using graphical tools like existing RDF visualization/exploration tools (e.g., [VOWL](http://vowl.visualdataweb.org/)) and graphical SPARQL query authoring solutions (e.g., [SparqlFilterFlow](http://sparql.visualdataweb.org/)).

## Authors

* **Christian Mader** - *Initial design and implementation*

## License

This project is licensed under the GPLv3 License - see the [LICENSE](LICENSE) file for details.

## Acknowledgments

This work was funded by the [bIoTope project](http://biotope.cs.hut.fi/).