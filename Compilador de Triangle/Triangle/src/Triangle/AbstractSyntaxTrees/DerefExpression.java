


package Triangle.AbstractSyntaxTrees;

import Triangle.SyntacticAnalyzer.SourcePosition;


public class DerefExpression extends Expression {

  public DerefExpression(PointerVname PV, SourcePosition thePosition) {
    super(thePosition);
    this.PV = PV;
  }

  @Override
  public Object visit(Visitor v, Object o) {
    return v.visitDerefExpression(this, o);
  }

  public PointerVname PV;
}