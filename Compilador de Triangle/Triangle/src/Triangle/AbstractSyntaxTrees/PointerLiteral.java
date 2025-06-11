

package Triangle.AbstractSyntaxTrees;

import Triangle.SyntacticAnalyzer.SourcePosition;

public class PointerLiteral extends Terminal {

  public PointerLiteral (String theSpelling, SourcePosition thePosition) {
    super (theSpelling, thePosition);
  }

  public Object visit(Visitor v, Object o) {
    return v.visitPointerLiteral(this, o);
  }

}
