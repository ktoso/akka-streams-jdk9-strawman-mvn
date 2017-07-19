package akka.stream.javadsl;

public class Source<T> {

  public static <T> Source<T> single(T element) {
    return new SingleSource<T>(element);
  }

  public <M> M runWith(Sink<T, M> sink) {
    return sink.mockMaterialize(this);
  }
}
