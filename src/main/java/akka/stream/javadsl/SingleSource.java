package akka.stream.javadsl;

public class SingleSource<T> extends Source<T> {
  private final T element;

  public SingleSource(T element) {
    this.element = element;
  }

  public T element() {
    return element;
  }
}
