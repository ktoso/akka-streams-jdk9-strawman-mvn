package com.example;

import akka.stream.javadsl.Sinks;
import akka.stream.javadsl.Source;

import java.util.concurrent.Flow;

public class MainJava9_IN_8 {

  public static void main(String[] args) {
    
    // even LOADING this class, which depends on the Flow interfaces would fail
    // if we don't hide it in JDK8:
    
    /*
    ktoso @ 三日月~/code/akka-streams-jdk9-strawman-mvn [master*]
    $ mvn package > /dev/null
    
    ktoso @ 三日月~/code/akka-streams-jdk9-strawman-mvn [master*]
    $ java -cp /Users/ktoso/code/akka-streams-jdk9-strawman-mvn/target/akka-streams-jdk9-strawman-mvn.jar:/Users/ktoso/.m2/repository/org/reactivestreams/reactive-streams/1.0.0/reactive-streams-1.0.0.jar com.example.MainJava9_IN_8
    
    Error: A JNI error has occurred, please check your installation and try again
    Exception in thread "main" java.lang.NoClassDefFoundError: java/util/concurrent/Flow$Subscriber
    	at java.lang.Class.getDeclaredMethods0(Native Method)
    	at java.lang.Class.privateGetDeclaredMethods(Class.java:2701)
    	at java.lang.Class.privateGetMethodRecursive(Class.java:3048)
    	at java.lang.Class.getMethod0(Class.java:3018)
    	at java.lang.Class.getMethod(Class.java:1784)
    	at sun.launcher.LauncherHelper.validateMainClass(LauncherHelper.java:544)
    	at sun.launcher.LauncherHelper.checkAndLoadMain(LauncherHelper.java:526)
    Caused by: java.lang.ClassNotFoundException: java.util.concurrent.Flow$Subscriber
    	at java.net.URLClassLoader.findClass(URLClassLoader.java:381)
    	at java.lang.ClassLoader.loadClass(ClassLoader.java:424)
    	at sun.misc.Launcher$AppClassLoader.loadClass(Launcher.java:335)
    	at java.lang.ClassLoader.loadClass(ClassLoader.java:357)
    	... 7 more
     */
  }

  public static java.util.concurrent.Flow.Subscriber<String> getMe() {
    return new Flow.Subscriber<String>() {
      @Override
      public void onSubscribe(Flow.Subscription subscription) {
      }

      @Override
      public void onNext(String item) {
      }

      @Override
      public void onError(Throwable throwable) {
      }

      @Override
      public void onComplete() {
      }
    };
  }
  
  static final org.reactivestreams.Subscriber<String> printlnRS = new org.reactivestreams.Subscriber<String>() {
    @Override
    public void onSubscribe(org.reactivestreams.Subscription subscription) {
      subscription.request(100);
    }

    @Override
    public void onNext(String s) {
      System.out.println("ReactiveStreams onNext = " + s);
    }

    @Override
    public void onError(Throwable throwable) {
      System.out.println("ReactiveStreams onError = " + throwable);
    }

    @Override
    public void onComplete() {
      System.out.println("ReactiveStreams onComplete()");
    }
  };

  static final Flow.Subscriber<String> printlnJDK = new Flow.Subscriber<String>() {
    @Override
    public void onSubscribe(Flow.Subscription subscription) {
      subscription.request(100);
    }

    @Override
    public void onNext(String s) {
      System.out.println("Flow.Publisher onNext = " + s);
    }

    @Override
    public void onError(Throwable throwable) {
      System.out.println("Flow.Publisher onError = " + throwable);
    }

    @Override
    public void onComplete() {
      System.out.println("Flow.Publisher onComplete()");
    }
  };


}
