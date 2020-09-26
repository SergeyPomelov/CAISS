# Computer and Algorithm Interaction Simulation Software (CAISS).
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

### Relevant Publications
1. Pomelov, S.V., Skopin, I.N. (2020). A Performance Prediction Method Showcase: Ant Colony Optimization Scaling for a Particular Computer System. Test Engineering and Management.
2. Pomelov, S.V. (2019). ACO Execution Simulation. Control Systems and Informational Technologies.
3. Pomelov, S.V. (2016). Simulation of the Algorithm Execution on a Given Architecture. Applied Informatics.
4. Pomelov, S.V. (2015). Computer and algorithm interaction simulation software (CAISS). Patent No. 2015618314.

# License
GNU GENERAL PUBLIC LICENSE Version 3.
