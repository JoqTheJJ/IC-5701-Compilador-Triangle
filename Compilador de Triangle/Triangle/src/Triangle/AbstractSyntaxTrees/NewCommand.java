package Triangle.AbstractSyntaxTrees;

import Triangle.SyntacticAnalyzer.SourcePosition;

public class NewCommand extends Expression {

  public NewCommand(TypeDenoter type, SourcePosition thePosition) {
    super(thePosition);
    this.type = type;
  }

  @Override
  public Object visit(Visitor v, Object o) {
    return v.visitNewCommand(this, o);
  }

  public TypeDenoter type;
}
