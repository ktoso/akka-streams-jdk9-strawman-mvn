package org.reactivestreams;

import java.util.concurrent.Flow;

public interface DefaultBridgesPublisher<T> extends Flow.Publisher<T>, org.reactivestreams.Publisher<T> {
  default public void subscribe(Subscriber<? super T> subscriber) {
    this.subscribe(new Subscriber<T>() { // TODO this would use the bridge one from reactive streams
      @Override
      public void onSubscribe(Subscription subscription) {
        
      }

      @Override
      public void onNext(T t) {

      }

      @Override
      public void onError(Throwable throwable) {

      }

      @Override
      public void onComplete() {

      }
    });
  }
}
