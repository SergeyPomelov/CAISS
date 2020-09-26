# Computer and algorithm interaction simulation software (CAISS).
![Computer and algorithm interaction simulation software (CAISS)](https://i.ibb.co/8mn6571/performance.jpg)

The simulation software contains inside its core representations of 
architecture and algorithm implemented with respect to a derived in 
a related PhD thesis mathematical model. It intended for simulation 
architecture and algorithm interactions and listing the results for 
optimization purposes. It, in addition, includes a few benchmarks for 
comparison a real performance with simulation results for improving and 
testing purposes.   

### Running in IDEA
First - import the gradle project. Then select the desired runnable 
class and run as a console Java application.
**benchmarks.ants.run.AntsRunner
benchmarks.matrixes.ManualBenchmarksRunner**

The next runnable needs a JMH plugin and annotations processing on.
**benchmarks.matrixes.JMHBenchmarksRunner** 

### Running via Gradle
gradle -jmh command runs the 
**benchmarks.matrixes.JMHBenchmarksRunner** 
without additional settings.

# License
GNU GENERAL PUBLIC LICENSE Version 3.
