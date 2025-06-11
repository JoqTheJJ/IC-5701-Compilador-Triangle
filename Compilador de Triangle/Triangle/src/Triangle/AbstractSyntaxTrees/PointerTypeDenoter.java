
package Triangle.AbstractSyntaxTrees;

import Triangle.SyntacticAnalyzer.SourcePosition;

public class PointerTypeDenoter extends TypeDenoter {

  public PointerTypeDenoter (TypeDenoter tAST, SourcePosition thePosition) {
    super (thePosition);
    this.T = tAST;
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
  
    @Override
    public String toString() {
        return "PointerTypeDenoter<" + this.T.toString() + ">";
    }
  
  public TypeDenoter T;
}
