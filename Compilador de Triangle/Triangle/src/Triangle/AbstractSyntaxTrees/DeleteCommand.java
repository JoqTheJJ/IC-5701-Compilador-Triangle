package Triangle.AbstractSyntaxTrees;

import Triangle.SyntacticAnalyzer.SourcePosition;

public class DeleteCommand extends Command {

  public DeleteCommand(Vname vAST, SourcePosition thePosition) {
    super(thePosition);
    V = vAST;
  }

  public Object visit(Visitor v, Object o) {
    return v.visitDeleteCommand(this, o);
  }

  public Vname V;
}
