

package Triangle.AbstractSyntaxTrees;

import Triangle.SyntacticAnalyzer.SourcePosition;

public class PointerVname extends Vname {

  public PointerVname (Identifier iAST, SourcePosition thePosition) {
    super (thePosition);
    I = iAST;
  }

  public Object visit (Visitor v, Object o) {
    return v.visitPointerVname(this, o);
  }

  public Identifier I;
}