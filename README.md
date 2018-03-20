# ExCELL TrafficStateService

The TrafficStateService analyzes the edges from the [Graphhopper](https://www.graphhopper.com/) model and classifies the traffic status (LOS 1-6).

*Currently only a dummy implementation!*

## Setup

This web service comes as a [SpringBoot](https://projects.spring.io/spring-boot/) application so it's very easy to test it on your local machine. 

### Build it

The project is using [Maven](https://maven.apache.org/) as a build tool and for managing the software dependencies. So in order to build the software you should install Maven on your machine. To create an executable JAR file for your local machine open you favourite shell environment and run:

<pre>mvn clean package</pre>

This creates a JAR file called `TrafficStateService.jar`.

### Run it

Run the JAR with the following. You might also want to change the server port:

<pre>java -jar TrafficStateService.jar</pre>


## API Doc

This projects provides a [Swagger](https://swagger.io/) interface to support the Open API initiative. The Java library [Springfox](http://springfox.github.io/springfox/) is used to automatically create the swagger UI configuration from annotations in the Java Spring code.


## Developers

Henning Jeske (TUD)

## Contact

* henning.jeske@tu-dresden.de

## Acknowledgement
The TravelTime-Service has been realized within the ExCELL project funded by the Federal Ministry for Economic Affairs and Energy (BMWi) and German Aerospace Center (DLR) - agreement 01MD15001B.


## Disclaimer

THIS SOFTWARE IS PROVIDED "AS IS" AND "WITH ALL FAULTS." 
BHS MAKES NO REPRESENTATIONS OR WARRANTIES OF ANY KIND CONCERNING THE 
QUALITY, SAFETY OR SUITABILITY OF THE SKRIPTS, EITHER EXPRESSED OR 
IMPLIED, INCLUDING WITHOUT LIMITATION ANY IMPLIED WARRANTIES OF 
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, OR NON-INFRINGEMENT.

IN NO EVENT WILL BHS BE LIABLE FOR ANY INDIRECT, PUNITIVE, SPECIAL, 
INCIDENTAL OR CONSEQUENTIAL DAMAGES HOWEVER THEY MAY ARISE AND EVEN IF 
BHS HAS BEEN PREVIOUSLY ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
