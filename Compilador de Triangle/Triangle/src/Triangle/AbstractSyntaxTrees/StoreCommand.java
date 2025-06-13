
package Triangle.AbstractSyntaxTrees;

import Triangle.SyntacticAnalyzer.SourcePosition;

public class StoreCommand extends Command {

  public StoreCommand(Vname vAST, Vname pointerAST, SourcePosition thePosition) {
    super(thePosition);
    V = vAST;
    pointer = pointerAST;
  }

  public Object visit(Visitor v, Object o) {
    return v.visitStoreCommand(this, o);
  }

  public Vname V;
  public Vname pointer;
}