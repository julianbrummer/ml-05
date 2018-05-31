To run just use the prebuild jar. 
e.g.
java -jar ml-05-1.0.0.jar [path_to_arff_file] [maxDepth (pass 0 to disable cv)] [numFolds] [outputFolder (optional)]

To compile/deploy the application yourself you need to have maven installed.
In the directory, containing pom.xml call:
mvn install