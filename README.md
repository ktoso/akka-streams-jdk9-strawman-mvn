# Adopting JDK8 Flow.* without hurting JDK8 Reactive Streams users with Akka Streams

This is a design proposal and proof-of-concept of using *multi-release jars*.

## Goals

Be as compatible, binary and source between versions, such that:

- just upgrading JDK8 to JDK9, but not using Flow.*, just using the existing Reactive Streams ecosystem,
- upgrading JDK8 to JDK9 and using the Flow.* interfaces,
- upgrading Akka Streams,

... is seamless, binary and source compatible, and trivial.

## Solution design

To be verified with Scala traits, but as far as I can see it can be easily pulled off:

- We introduce a new trait, similar to [FlowOps](https://github.com/akka/akka/blob/master/akka-stream/src/main/scala/akka/stream/scaladsl/Flow.scala#L519)
  - and the same for `Source` / `Sink` of course
  - It could be called like `JdkFlowSupport` etc
- that trait is *empty* in `src/main/scala`!
- similar as this proof-of-concept, we (create support for and...) use `src/main/scala-jdk9`
- there we put the same trait, but for JDK9 it actually contains the methods.
  - to disambiguate, we will have to call them like `toFlowPublisher` and `fromFlowPublisher` etc
- these methods are only available for JKD9 users basically, and do not polute namespace for JDK8 users

Internally:

- we keep using RS interfaces, since we can not demant the presence of `Flow.*` on classpath
- if we get or expose RS interfaces, we just do so as usual
- if we get or expose JDK `Flow.*` interfaces, we adapt them to RS
  - note that the semantics are 1:1 exactly the same, it's just a simple type adaptation
  - we can, but do not have to, use the reactive-stream-adapters provided library, since it's internal for us

In future internally:

- once we can require JDK9 as minimum JDK level, could be some years..., we can then just change all interfaces using RS internally to JDK's Flow.*
  - this is a trivial change, since the semantics are the same
- places where we exposed RS now need to be adapted - good, so we're adapting to (by then) legacy interfaces, and the "normal one" does not need adaptation
- places where we exposed JDK `Flow.*` types, can drop the adaptation layer, shedding some overhead - this is the place we want to be in, great!

In future externally:

- No API changes, everyone keeps calling the same APIs, even when we "moved off from" RS interfaces internally

## Dependencies

- In order to pull this off in Akka we will have to develop the multi-release feature for sbt: https://github.com/sbt/sbt/issues/3036
- For this to be useful in practice JDK9 should be out of early access, however we can and should prepare the artifacts earlier so people can use JDK9 Flow.* as quickly as they want, especially since Akka Streams is at the core of the vibrant integartion ecosystem that Reactive Streams and [Alpakka](https://github.com/akka/alpakka) have created

# Visual explanation

Usage:

```
master u+3!akka-streams-jdk9-strawman-mvn *> mvn clean package > /dev/null

== JDK8 user ==

master u+3!akka-streams-jdk9-strawman-mvn *> ./8.sh
master u+3!akka-streams-jdk9-strawman-mvn *> java -version
java version "1.8.0_131"
Java(TM) SE Runtime Environment (build 1.8.0_131-b11)
Java HotSpot(TM) 64-Bit Server VM (build 25.131-b11, mixed mode)
master u+3!akka-streams-jdk9-strawman-mvn *> java -cp $HOME/.m2/repository/org/reactivestreams/reactive-streams/1.0.0/reactive-streams-1.0.0.jar:target/akka-streams-jdk9-strawman-mvn.jar com.example.MainJava8
ReactiveStreams onNext = hello
ReactiveStreams onComplete()

== JDK9 user == 

master u+3!akka-streams-jdk9-strawman-mvn *> ./9.sh
master u+3!akka-streams-jdk9-strawman-mvn *> java -version
java version "9-ea"
Java(TM) SE Runtime Environment (build 9-ea+174)
Java HotSpot(TM) 64-Bit Server VM (build 9-ea+174, mixed mode)

# "old code" just works, continues to work with existing JDK8 methods etc
master u+3!akka-streams-jdk9-strawman-mvn *> java -cp $HOME/.m2/repository/org/reactivestreams/reactive-streams/1.0.0/reactive-streams-1.0.0.jar:target/akka-streams-jdk9-strawman-mvn.jar com.example.MainJava8
ReactiveStreams onNext = hello
ReactiveStreams onComplete()

# "new code" can use JDK9 instead
master u+3!akka-streams-jdk9-strawman-mvn *> java -cp $HOME/.m2/repository/org/reactivestreams/reactive-streams/1.0.0/reactive-streams-1.0.0.jar:target/akka-streams-jdk9-strawman-mvn.jar com.example.MainJava9
Flow.Publisher onNext = hello
Flow.Publisher onComplete()
```

- JDK8 users are not tempted to use JDK9 interfaces 
  - since IDEs should not even display those methods - since they're "hiden" (see the jar format below)
- JDK9 users can do whatever they want
- we're not forced to set target level to 9
  - so can ship such version ASAP


The JAR file looks like this:

```
...
akka/stream/javadsl/JdkDependentSinks.class
...
META-INF/versions/9/akka/stream/javadsl/JdkDependentSinks.class
```

The one in `META-INF/versions/9` is loaded *instead* of the "normal" one if running on JDK9.
