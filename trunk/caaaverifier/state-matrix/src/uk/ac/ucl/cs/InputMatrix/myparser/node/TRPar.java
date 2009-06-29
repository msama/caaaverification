/* This file was generated by SableCC (http://www.sablecc.org/). */

package uk.ac.ucl.cs.InputMatrix.myparser.node;

import uk.ac.ucl.cs.InputMatrix.myparser.analysis.*;

public final class TRPar extends Token
{
    public TRPar()
    {
        super.setText(")");
    }

    public TRPar(int line, int pos)
    {
        super.setText(")");
        setLine(line);
        setPos(pos);
    }

    public Object clone()
    {
      return new TRPar(getLine(), getPos());
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseTRPar(this);
    }

    public void setText(String text)
    {
        throw new RuntimeException("Cannot change TRPar text.");
    }
}
