package Triangle.AbstractSyntaxTrees;

import Triangle.SyntacticAnalyzer.SourcePosition;

public class NewExpression extends Expression {

  public NewExpression(TypeDenoter type, SourcePosition thePosition) {
    super(thePosition);
    this.type = type;
  }

  @Override
  public Object visit(Visitor v, Object o) {
    return v.visitNewExpression(this, o);
  }

  public TypeDenoter type;
}
