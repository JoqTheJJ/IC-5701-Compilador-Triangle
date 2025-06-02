package Triangle.AbstractSyntaxTrees;

import Triangle.SyntacticAnalyzer.SourcePosition;

public class NewCommand extends Command {

  public NewCommand(Vname vAST, SourcePosition thePosition) {
    super(thePosition);
    V = vAST;
  }

  @Override
  public Object visit(Visitor v, Object o) {
    return v.visitNewCommand(this, o);
  }

  public Vname V;
}
