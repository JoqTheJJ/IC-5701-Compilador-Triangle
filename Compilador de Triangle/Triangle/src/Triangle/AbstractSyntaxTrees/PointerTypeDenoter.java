
package Triangle.AbstractSyntaxTrees;

import Triangle.SyntacticAnalyzer.SourcePosition;

public class PointerTypeDenoter extends TypeDenoter {

  public PointerTypeDenoter (SourcePosition thePosition) {
    super (thePosition);
  }

  public Object visit (Visitor v, Object o) {
    return v.visitPointerTypeDenoter(this, o);
  }

  public boolean equals (Object obj) {
    if (obj != null && obj instanceof ErrorTypeDenoter)
      return true;
    else
      return (obj != null && obj instanceof PointerTypeDenoter);
  }
}
