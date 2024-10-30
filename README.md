# Computer and Algorithm Interaction Simulation Software (CAISS).

![Repository Top Language](https://img.shields.io/github/languages/top/SergeyPomelov/CAISS)
![Languages](https://img.shields.io/github/languages/count/SergeyPomelov/CAISS)
![Github Repository Size](https://img.shields.io/github/repo-size/SergeyPomelov/CAISS)
![Github Open Issues](https://img.shields.io/github/issues/SergeyPomelov/CAISS)
![License](https://img.shields.io/badge/license-MIT-green)

![Computer and algorithm interaction simulation software (CAISS)](https://i.ibb.co/PD5B7By/web-performance-monitoring-101.jpg)

The simulation software’s core contains representations of architecture and algorithm implemented atop of a mathematical model derived in a related Ph.D thesises. It is intended for simulation architecture and algorithm interactions and listing the results for optimization purposes. Besides, it includes a few benchmarks for comparison a real performance with simulation results for improving and testing purposes. 

### Running in IDEA
First - import the Gradle project. Then select the desired runnable 
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
