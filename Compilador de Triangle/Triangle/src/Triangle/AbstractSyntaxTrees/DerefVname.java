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
public class DerefVname extends Vname {
    public Vname V;
    
    public DerefVname(Vname vAST, SourcePosition thePosition) {
        super(thePosition);
        V = vAST;
    }
    
    public Object visit(Visitor v, Object o) {
        return v.visitDerefVname(this, o);
    }
}
