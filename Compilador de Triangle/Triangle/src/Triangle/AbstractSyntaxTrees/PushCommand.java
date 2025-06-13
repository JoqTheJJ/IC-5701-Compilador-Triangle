
package Triangle.AbstractSyntaxTrees;

import Triangle.SyntacticAnalyzer.SourcePosition;


public class PushCommand extends Command {
  public PushCommand(Vname vAST, SourcePosition thePosition) {
    super(thePosition);
    V = vAST;
  }

  public Object visit(Visitor v, Object o) {
    return v.visitPushCommand(this, o);
  }

  public Vname V;
}
