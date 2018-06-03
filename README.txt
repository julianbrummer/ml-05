To run just use the prebuild jar:
java -jar ml-05-1.0.0.jar [path_to_training_file] [path_to_test_file] [path_to_output_file] [vocabulary_size (optional, default=5000)]

e.g. run:

java -jar ml-05-1.0.0.jar train3500.txt test.txt classification.txt

To compile/deploy the application yourself you need to have maven installed.
In the directory, containing pom.xml call:
mvn install