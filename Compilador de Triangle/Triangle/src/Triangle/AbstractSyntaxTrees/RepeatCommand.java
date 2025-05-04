/*
  #Repeat Command
 */
package Triangle.AbstractSyntaxTrees;

import Triangle.SyntacticAnalyzer.SourcePosition;

public class RepeatCommand extends Command {

  public RepeatCommand (Expression eAST, Command cAST, SourcePosition thePosition) {
    super (thePosition);
    E = eAST;
    C = cAST;
  }

  public Object visit(Visitor v, Object o) {
    return v.visitRepeatCommand(this, o);
  }

  public Expression E;
  public Command C;
}