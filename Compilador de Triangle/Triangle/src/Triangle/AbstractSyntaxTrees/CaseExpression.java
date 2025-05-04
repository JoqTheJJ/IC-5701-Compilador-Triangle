
package Triangle.AbstractSyntaxTrees;

import java.util.List;

public class CaseExpression {
  
  public CaseExpression(List<Expression> cases, Expression eAST) {
    this.cases = cases;
    E = eAST;
  }
  
  public List<Expression> cases;
  public Expression E;
}