
package Triangle.AbstractSyntaxTrees;

import java.util.List;

public class Case {
  
  public Case(List<Expression> cases, Command command) {
    this.cases = cases;
    C = command;
  }
  
  public List<Expression> cases;
  public Command C;
}