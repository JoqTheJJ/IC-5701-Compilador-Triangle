
package Triangle.AbstractSyntaxTrees;

import Triangle.SyntacticAnalyzer.SourcePosition;

public class PointerTypeDenoter extends TypeDenoter {

  public PointerTypeDenoter (Identifier iAST, SourcePosition thePosition) {
    super (thePosition);
    I = iAST;
    T = null;
  }
  
  public void setPointerType(TypeDenoter pointerType){
    this.T = pointerType;
    String type = String.valueOf(T);
    System.out.println("Holi soy (toString): " + type);
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
  
  public Identifier I;
  public TypeDenoter T;
}
