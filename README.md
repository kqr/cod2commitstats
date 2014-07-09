cod2commitstats
===============

A little Java tool to parse cod2 multiplayer log files and put the interesting information in a database.


Dependencies
------------

Depends on opencsv and postgresql-jdbc!


Building
--------

Compile with

    javac -classpath ".:postgresql-9.3-1101.jdbc41.jar:opencsv-2.3.jar" UpdateStats.java

Run as

    java Updatestats <log file> <postgres server> <database> <username> <password>

Create the tables as shown in `postgres-tables.sql` first!
