# Eriantys - Software Engineering Project

Eriantys is the final test of "Software Engineering", course of "Computer Science Engineering" 
held at Politecnico di Milano (2021/2022).

**Professor**: Gianpaolo Cugola

**Group**: GC-45

## Implemented Functionalities

| Functionality           | Status             |
|-------------------------|--------------------|
| Basic Rules             | :heavy_check_mark: |
| Complete Rules          | :heavy_check_mark: |
| CLI                     | :heavy_check_mark: |
| GUI                     | :heavy_check_mark: |
| Socket                  | :heavy_check_mark: |
| Character Cards (FA 1)  | :heavy_check_mark: |
| 4 Players Match (FA 2)  | :heavy_check_mark: |
| Multiple Matches (FA 3) | :heavy_check_mark: |

## Testing

| Package    | Class          | Coverage      |
|------------|----------------|---------------|
| Model      | Entire Package | 94% (720/763) |
| Controller | Entire Package | 82% (250/303) |
| Controller | ClientHandler  | 69% (53/76)   |
| Controller | Controller     | 90% (166/184) |
| Controller | Server         | 72% (31/43)   | 

## Students

- Andrea Piras (ID Number: 936909)
- Andrea Sanguineti (ID Number: 936930)
- Emanuele Santoro (ID Number: 933198)

## Compile

To run the test and compile the software:

1. Install [Java SE Development Kit 18](https://www.oracle.com/java/technologies/javase/jdk18-archive-downloads.html)
2. 

## Run using the JAR file

Once installed all requirements and compiled the project, open a terminal and
go to the project target directory. 
Once there it is possible to choose to run the server or the client (CLI or GUI):

### Run the Server
```bash
java -jar GC45-<version>-jar-with-dependencies.jar -s 
```
or
```bash
java -jar GC45-<version>-jar-with-dependencies.jar --server 
```
### Run the Client (CLI)
```bash
java -jar GC45-<version>-jar-with-dependencies.jar -c 
```
or
```bash
java -jar GC45-<version>-jar-with-dependencies.jar --cli 
```

### Run the Client (GUI)
```bash
java -jar GC45-<version>-jar-with-dependencies.jar 
```
To run the Client (GUI) it is also possible to open the JAR file directly from
the file explorer.

### Recommendations

In order to play, you'll have to launch at least one server and two clients (either CLI or GUI).

WARNING: For the best GUI experience it is strongly suggested to play with a screen resolution
of 1920x1080 (100% DPI) and with a scaling of 100%.






