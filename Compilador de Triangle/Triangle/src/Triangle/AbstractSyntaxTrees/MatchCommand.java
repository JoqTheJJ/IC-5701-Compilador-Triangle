package Triangle.AbstractSyntaxTrees;

import Triangle.SyntacticAnalyzer.SourcePosition;
import java.util.List;

public class MatchCommand extends Command {

  public MatchCommand(Expression expression, List<Case> cases,
          Command otherwiseCase, SourcePosition thePosition) {
    super(thePosition);
    E = expression;
    C = cases;
    O = otherwiseCase;
  }

  public Object visit(Visitor v, Object o) {
    return v.visitMatchCommand(this, o);
  }

  public Expression E;
  public List<Case> C;
  public Command O;
}
