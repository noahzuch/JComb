<!--
*** Thanks for checking out this README Template. If you have a suggestion that would
*** make this better, please fork the repo and create a pull request or simply open
*** an issue with the tag "enhancement".
*** Thanks again! Now go create something AMAZING! :D
-->





<!-- PROJECT SHIELDS -->
<!--
*** I'm using markdown "reference style" links for readability.
*** Reference links are enclosed in brackets [ ] instead of parentheses ( ).
*** See the bottom of this document for the declaration of the reference variables
*** for contributors-url, forks-url, etc. This is an optional, concise syntax you may use.
*** https://www.markdownguide.org/basic-syntax/#reference-style-links
-->




<!-- PROJECT LOGO -->
 <br />
 <h1 align="center">JComb</h1>
<p align="center">
   A JUnit extension for automated combinatorial testing!
  <p align="center">
    <br />
    <a href="https://github.com/othneildrew/Best-README-Template/issues">Report Bug</a>
    ·
    <a href="https://github.com/othneildrew/Best-README-Template/issues">Request Feature</a>
  </p>
</p>



<!-- TABLE OF CONTENTS -->
## Table of Contents

* [About the Project](#about-the-project)
* [Installation](#installation)
* [Getting Started](#getting-started)
* [License](#license)
* [Contact](#contact)



<!-- ABOUT THE PROJECT -->
## About The Project
Combinatorial testing is an effective test case generation technique that is based on the observation that most faults are caused by interactions of only a few (mostly two or three) parameters. A test case with t-wise combinatorial coverage is a set of tests that, for every combination of t parameters all possible discrete combinations of parameter values are covered. *JComb* is an extension for <a href="https://github.com/junitteam/junit5">JUnit5</a> and allows a user to define and execute automated combinatorial tests directly in your test class. It works similarly to parameterized testing as where parameterized test method is executed multiple times to achieve combinatorial coverage.

JComb allows you to:
* Define parameters directly in your test class
* Define constraints for unwanted combinations
* Reuse parameters and constraints over multiple test classes
* Use complex java objects as values for parameters

A simple example class using JComb could be:
```java
//Activate the JComb extension
@ExtendWith(JCombExtension.class)
class CombTest {

    //Define parameters
    @Parameter(0)
    static Ints intParameter = new Ints(0, 1, 2);
    
    @Parameter(1)
    static Values stringParameter = new Values("1", "2", "3");
    
    @Parameter(2)
    static Values objectParameter = new Values(
    Arrays.asList(new int[]{1,2,3}),
    Collections.EMPTY_LIST);

    //Define a testmethod
    @JCombTest()
    void test(int p1, String p2, List<Integer> p3) {
        //...
    }
}
```

<!-- Installation -->
## Installation
JComb is available on JCenter. Get it for your Maven or Gradle project:
```maven
<dependency>
    <groupId>com.noahzuch.jcomb</groupId>
    <artifactId>JComb</artifactId>
    <version>1.0.0</version>
    <scope>test</scope>
</dependency>
```

or

```gradle
testImplementation 'com.noahzuch.jcomb:JComb:1.0.0'
```

<!-- USAGE EXAMPLES -->
## Getting Started
### Basics
To start creating tests with JComb the class containing the test method has to be annotated with `@ExtendWith(JCombExtension.class)`. Then Parameters can be defined via the `@Parameter` annotation, Constraints via the `@Constraint` annotation and test methods that will use JComb via the `@JCombTest` annotation.

### Adding Parameters
The first step for creating a combinatorial test with JComb is to define the necessary parameters. A parameter is simply an annotated field whose type is a subclass of the `Domain` class. The most prominent ways to define a parameter is using the `Values` class or the corresponding classes for primitives (e.g. `Ints` or `Doubles`), but creating custom subclasses of `Domain` is also allowed. Each annotation requires a unique index, which will be used to refer to this specific parameter. Parameters can be defined static or nonstatic. A static parameter gets initialized only once for the whole combinatorial test and changes to the value of the parameter (if the parameter is an object and not a primitive) get propagated to the next test execution. A nonstatic parameter, on the other hand, gets reinitialized for every single test execution in the combinatorial test.

### Creating test methods
To use JComb for a test method, the method has to be annotated with `@JCombTest` instead of the normal `@Test` annotation. For every parameter in the combinatorial test, the method has to accept a corresponding input parameter. The order of these parameters has to match the natural order of their indices.

### Using Constraints
To exclude some combinations of values from being executed in a test method, constraints are needed. In JComb Constraints are defined via annotated methods. A constraint method checks if a given combination of some parameter values should be allowed in the test or not. A simple example of a constraint method could be:
```java
 @Parameter(0)
 private static Ints parameter1 = new Ints(1, 2, 3);
  
 @Parameter(1)
 private static Values parameter2 = new Values("1", "2", "3");
 
 @Parameter(2)
 private static Doubles parameter3 = new Doubles(1d, 2d, 3d);
  
 @Constraint(id=0, parameters={0,1})
 public static boolean checkNotEqual(int param1, Strint param2){
     return param1 != Integer.parseInt(param2);
 }
```
This constraint excludes all combinations of the first two parameters, which represent the same numerical value. Constraints should be deterministic and only use to the supplied parameters to decide if a combination is allowed or not. Therefore a constraint method has to always be static.

### Inheritance
Oftentimes some parameters or constraints could be reused for multiple test classes. JComb allows this in two ways:
* Subclasses inherit all the parameters and constraints from their parent class
* The `@InheritSetup` annotation can be used to include parameters and constraints from any other class.
To easily reuse important parameters over multiple test classes, it helps creating a separate setup class, that contains only parameters and constraints, and to refer to this class via `@InheritSetup` in the actual test class.
It is recommended to use constant integer fields as indices, to more easily reference parameters and constraints in different classes:
```java
public static final int PARAMETER_0 = 0;
@Parameter(PARAMETER_0)
private Values parameter0 = new Values(...);
```

### Subsets of parameters and constraints
It is possible to not use all the defined parameters and constraints for a testmethod. Use the `@JCombTest` annotation to define which parameters/constraints should be used:
```java
@JCombTest(parameters = {2,1}, constraints = {1})
void test(String param1, int param2){
    ...
}
```
A few things to watch out for:
* The order of the input parameter for the test method has to match the order of the parameter array in the annotation now and *not* the normal order.
* Constraints, where not all the required parameters are included in the parameter list of a test method, are simply ignored. So consider creating multiple small constraints instead of one big one.

<!-- LICENSE -->
## License

Distributed under the Apache 2.0 License. See `LICENSE` for more information.



<!-- CONTACT -->
## Contact

Noah Zuch - noahz97@gmail.com

Project Link: [https://github.com/Azuon79/JComb](https://github.com/Azuon79/JComb/)
