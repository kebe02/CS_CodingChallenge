# Documentation
This application reads large logfiles of process events, collects statistics about their runtimes and issues alerts if a process was too slow (>=4ms).
All created data is stored in an HSQLDB database which is automatically created and initialized in the directory where the application is run from.
For convenience a small test logfile is present in the project directory.

# Prerequisites
- Java 9
- Maven

# Build & Test
- Change into the project directory
- Run "mvn test" to run all unit tests
- Run "mvn package" to build an executable jar

# Run
- Run java -jar ./target/CodingAssignment-1.0-SNAPSHOT-jar-with-dependencies.jar logfile.txt from the project directory after building
