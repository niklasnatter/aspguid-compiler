# aspguid-compiler
This repository contains the source code of the _aspguid_ compiler, which enables the compilation of ASP encodings annotated with the _aspguid_ language.
The _aspguid_ compiler and allows 
to **generate** the Java application source code for an annotated ASP encoding,
to **compile** an annotated ASP encoding into an executable .jar application and 
to directly **execute** an annotated ASP encoding for testing purposes.

An executable version of the most recent _aspguid_ compiler is available as [aspguidc.jar](https://nnatter.github.io/aspguid-compiler/artifact/aspguidc.jar). 
A full implementation documentation is accessible in the [javadoc-format](https://nnatter.github.io/aspguid-compiler/docs/).
An example application developed using the _aspguid_ approach is presented in the [aspguid-project-planning](https://github.com/nnatter/aspguid-project-planning) repository.

## aspguid
_aspguid_ is a new approach for implementing applications usable by users without prior logic programming experience based on annotated ASP programs.
The approach consists of a declarative language for defining graphical user interfaces for ASP programs 
and a compiler that translates ASP programs which are annotated with such definitions into procedural code, that realizes respective graphical user interface assisted applications.

The _aspguid_ techniques significantly reduce the development overhead and minimize the repetitive workload for developers, 
compared to the current state of the art solution of embedding ASP programs into manually developed imperative graphical user interfaces. 

A complete introduction into the _aspguid_ approach is available in the [pdf-format](https://nnatter.github.io/aspguid-compiler/docs/aspguid-thesis.pdf).

## Usage
````
java -jar aspguidc.jar {-g|-c|-e} input_file
  -g  generate java source code for the annotated asp encoding
  -c  compile annotated asp encoding into executable .jar
  -e  execute annotated asp encoding
````

## Requirements
* JDK<sup>[1](http://www.oracle.com/technetwork/java/javase/downloads/index.html)</sup> of version 8 or above
* OpenJFX<sup>[2](http://openjdk.java.net/)</sup> (only when using [OpenJDK](http://openjdk.java.net/projects/openjfx/))

Requirements of applications compiled by the _aspguid_ compiler are presented in the [aspguid-project-planning](https://github.com/nnatter/aspguid-project-planning) repository.

## Screenshots
<div align="center">
<img src="https://user-images.githubusercontent.com/13310795/27508743-391e0688-58ec-11e7-8186-0df030caaf9c.png" width="350" hspace="5">
<img src="https://user-images.githubusercontent.com/13310795/27508744-393a6c74-58ec-11e7-8d8f-e9036c920cab.png" width="350" hspace="5">
</div>
