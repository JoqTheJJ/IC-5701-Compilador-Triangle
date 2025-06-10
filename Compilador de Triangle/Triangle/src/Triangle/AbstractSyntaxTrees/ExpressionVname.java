


package Triangle.AbstractSyntaxTrees;

import Triangle.SyntacticAnalyzer.SourcePosition;

public class ExpressionVname extends Vname {

  public ExpressionVname (Identifier iAST, SourcePosition thePosition) {
    super (thePosition);
    I = iAST;
  }

  public Object visit (Visitor v, Object o) {
    return v.visitSimpleVname(this, o);
  }

  public Identifier I;
}