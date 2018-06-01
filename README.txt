To run just use the prebuild jar:
java -jar ml-05-1.0.0.jar [path_to_training_file] [vocabulary_size]

e.g.:

java -jar ml-05-1.0.0.jar train3500.txt 1000 

To compile/deploy the application yourself you need to have maven installed.
In the directory, containing pom.xml call:
mvn install