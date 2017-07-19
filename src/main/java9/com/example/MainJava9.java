package com.example;

import akka.stream.javadsl.Sinks;
import akka.stream.javadsl.Source;

import java.util.concurrent.Flow;

public class MainJava9 {

  public static void main(String[] args) {
    
    
    // this exists only to emulate what Scala will do with traits and putting them into classes
    final Sinks Sink = Sinks.MODULE_CHANGE_DEFAULT;

//    // existing users, changing to JDK9  should still work with Reactive Streams
//    final org.reactivestreams.Publisher<String> rs = 
//      Source.single("hello").runWith(Sink.asPublisher());
//    rs.subscribe(printlnRS);

    Flow.Publisher<String> jdk = 
      Source.single("hello").runWith(Sink.asFlowPublisher());
    jdk.subscribe(printlnJDK);
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
