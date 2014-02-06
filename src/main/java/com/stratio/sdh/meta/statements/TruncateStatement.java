package com.stratio.sdh.meta.statements;

//import org.antlr.runtime.Token;

import com.stratio.sdh.meta.structures.Path;


public class TruncateStatement extends Statement {
    
    private String ident;
    
    public TruncateStatement(String ident){
        this.ident = ident;
    }

    public String getIdent() {
        return ident;
    }

    public void setIdent(String ident) {
        this.ident = ident;
    }        

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Truncating "+ident);
        return sb.toString();        
    }

    @Override
    public Path estimatePath() {
        return Path.CASSANDRA;
    }
    
}
