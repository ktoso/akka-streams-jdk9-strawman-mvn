package akka.stream.javadsl;

// See also Sinks which acts like the static / companion object
// we'll be able to pull that off in Scala thanks to methods being copied 
// from traits into classes, also generatic static scope for them, not possible 
// to pull off in plain Java AFAICS, since static method in interface should be called on interface 
public abstract class Sink<T, M> {
  abstract M mockMaterialize(Source<T> source);
}
