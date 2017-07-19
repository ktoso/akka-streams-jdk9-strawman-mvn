package akka.stream.javadsl;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.Flow;

/**
 * This class is mirroring the one for JDK8, but adds the JDK9 typed method, 
 * internally we can implement it as the same thing of course.
 */
public interface JdkDependentSinks {
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

          @SuppressWarnings("Duplicates")
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
  
  
  /**
   * JDK9-only method
   * 
   * Return typed as Flow.Publisher, internally we can implement using RS still
   * just this class has to require JDK9. Once we move to requiring JDK9, we switch 
   * the way which we adapt and we use Flow.Publisher everywhere, and adapt TO RS - 
   * only once we can require JDK9 though...
   * 
   * Overloading asPublisher won't work, so we have to say FlowPublisher, 
   * which probably is good anyway.
   */
  public default <T> Sink<T, Flow.Publisher<T>> asFlowPublisher() {
    return new Sink<T, Flow.Publisher<T>>() {
      Flow.Publisher<T> mockMaterialize(Source<T> source) {
        // just mock impl, not fully compliant
        return new Flow.Publisher<T>() {
          private volatile boolean done = false;

          @SuppressWarnings("Duplicates")
          public void subscribe(final Flow.Subscriber<? super T> subscriber) {
            subscriber.onSubscribe(new Flow.Subscription() {
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
