/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Triangle.AbstractSyntaxTrees;

import Triangle.SyntacticAnalyzer.SourcePosition;

/**
 *
 * @author thelo
 */
public class ReturnCommand extends Command {
    
    public ReturnCommand(Vname vAST, SourcePosition thePosition){
        super(thePosition);
        V = vAST;
    }
    
    public Object visit(Visitor v, Object o) {
        return v.visitReturnCommand(this, o);
    }
    
    public Vname V;
}
