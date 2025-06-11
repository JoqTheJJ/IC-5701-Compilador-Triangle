

package Triangle.AbstractSyntaxTrees;

import Triangle.SyntacticAnalyzer.SourcePosition;

public class PointerExpression extends Expression {

  public PointerExpression (PointerLiteral plAST, SourcePosition thePosition) {
    super (thePosition);
    PL = plAST;
  }

  public Object visit(Visitor v, Object o) {
    return v.visitPointerExpression(this, o);
  }

  public PointerLiteral PL;
}