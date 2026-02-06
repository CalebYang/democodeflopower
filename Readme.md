1) Instructions on how to run

To run, install Java 21

https://www.oracle.com/asean/java/technologies/downloads/#jdk21-windows

Take jar file from lib folder

Type following in command prompt

java -cp meter_reader.jar org.flowenergy.csv.DataParser <data_filename>

Sample is provided in src/data directory

To import project and review code,

Download Eclipse

https://eclipseide.org/

After that, click File > New Project > Java

Change the workspace directory to the checkout directory

Main class is org.flowenergy.csv.DataParses


Interview Questions

1) Why did I use the technology I used
I am a Java coder, so that's what I am most familiar with

2) What would I have done differently if I had the time?
This is merely a demo. And the document stated it was a half day job.

Based on those constraints, it was not a full implementation of parsing the file. It was merely just read for what was needed and contained basic validation

The fields as well as the integrity of the file would have to be checked also, more decisions have to be made about how a dirty file is to be handled.

3) What are the rationale for decisions made?

The original plan was to just parse the data as is and then from there reconstruct it, but having read the spec that it needs to run in prod, 300 entries had their reading flattened to a sum so Java could do garbage collection if data would be too much

There was an option to create a cleaner version without completely parsing the objects in their original form, but considering both the time frame, the extensibility of the current solution as well as the rework needed to do that, it was kept as is.
