/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Triangle.AbstractSyntaxTrees;

import Triangle.SyntacticAnalyzer.SourcePosition;

public class ForCommand extends Command {

  public ForCommand (Vname vAST, Expression e1AST, Expression e2AST,
                     Command cAST, int loopDirection, SourcePosition thePosition) {
    super (thePosition);
    V = vAST;
    E1 = e1AST;
    E2 = e2AST;
    C = cAST;
    Direction = loopDirection;
  }

  public Object visit(Visitor v, Object o) {
    return v.visitForCommand(this, o);
  }

  public Vname V;
  public Expression E1;
  public Expression E2;
  public Command C;
  public int Direction;
}

