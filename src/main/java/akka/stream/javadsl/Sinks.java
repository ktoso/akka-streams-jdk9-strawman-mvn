package akka.stream.javadsl;

public class Sinks implements JdkDependentSinks {
  public Sinks() {
  }
  
  // I don't think we should do this, but it's possible:
  public static Sinks MODULE_CHANGE_DEFAULT = new Sinks();  
}
