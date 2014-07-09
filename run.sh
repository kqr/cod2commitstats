
cp=".:sqlite-jdbc-3.7.2.jar:opencsv-2.3.jar"

javac $1 -classpath $cp UpdateStats.java && java -classpath $cp UpdateStats

