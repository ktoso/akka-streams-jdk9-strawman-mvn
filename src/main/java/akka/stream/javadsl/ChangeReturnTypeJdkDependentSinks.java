package akka.stream.javadsl;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public interface ChangeReturnTypeJdkDependentSinks {
  /**
   * Acts AS IF existing method on Sink in Akka 2.5.x.
   * So it returns Reactive Streams interfaces.
   */
  public default <T> Sink<T, Publisher<T>> asPublisher() {
    return new Sink<T, Publisher<T>>() {
      Publisher<T> mockMaterialize(Source<T> source) {
        // just mock impl, not fully compliant
        return new Publisher<T>() {
          private volatile boolean done = false;

          public void subscribe(final Subscriber<? super T> subscriber) {
            subscriber.onSubscribe(new Subscription() {
              public void request(long n) {
                try {
                  if (n > 0 && !done) {
                    final SingleSource<T> singleSource = (SingleSource<T>) source; // just a hack, would be actual materializer
                    subscriber.onNext(singleSource.element());
                    subscriber.onComplete();
                  } else {
                    subscriber.onError(new IllegalArgumentException("Illegal request, must be > 0"));
                  }

                } catch (ClassCastException ex) {
                  subscriber.onError(ex);
                }
                
                // can only signal one thing
                done = true;
              }

              public void cancel() {
                done = true;
              }
            });
          }
        };
      }
    };
  }
  
}
