package com.example;

import akka.stream.javadsl.Sinks;
import akka.stream.javadsl.Source;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public class MainJava8 {

  public static void main(String[] args) {
    // this exists only to emulate what Scala will do with traits and putting them into classes
    final Sinks Sink = Sinks.MODULE_CHANGE_DEFAULT;

    // existing users, Akka 2.5 and Reactive Streams, Java 8 API
    org.reactivestreams.Publisher<String> rs =
      Source.single("hello").runWith(Sink.asPublisher());

    rs.subscribe(printlnRS);

    // JDK8 users can not use JDK9 interfaces, obviously:
    //    
    //    Not available:
    //    Flow.Publisher<String> pub = 
    //      Source.single("hello").runWith(Sink.asFlowPublisher());
  }

  static final Subscriber<String> printlnRS = new Subscriber<String>() {
    @Override
    public void onSubscribe(Subscription subscription) {
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


}
