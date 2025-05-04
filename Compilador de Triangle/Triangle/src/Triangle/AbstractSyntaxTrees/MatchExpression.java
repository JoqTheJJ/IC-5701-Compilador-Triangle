package Triangle.AbstractSyntaxTrees;

import Triangle.SyntacticAnalyzer.SourcePosition;
import java.util.List;

public class MatchExpression extends Expression {
    
  public MatchExpression(Expression expression, List<CaseExpression> cases,
          Expression otherwiseCase, SourcePosition thePosition) {
    super(thePosition);
    E = expression;
    C = cases;
    O = otherwiseCase;
  }

  public Object visit(Visitor v, Object o) {
    return v.visitMatchExpression(this, o);
  }

  public Expression E;
  public List<CaseExpression> C;
  public Expression O;
}