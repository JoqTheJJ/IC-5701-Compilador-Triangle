

package Triangle.AbstractSyntaxTrees;

import Triangle.SyntacticAnalyzer.SourcePosition;

public class PointerLiteral extends Terminal {

  public PointerLiteral (String theSpelling, String name, SourcePosition thePosition) {
    super (theSpelling, thePosition);
    this.name = name;
  }

  public Object visit(Visitor v, Object o) {
    return v.visitPointerLiteral(this, o);
  }
  
  public final String name;
}
