/* This file was generated by SableCC (http://www.sablecc.org/). */

package uk.ac.ucl.cs.InputMatrix.myparser.node;

import uk.ac.ucl.cs.InputMatrix.myparser.analysis.*;

public final class TVariableAbbr extends Token
{
    public TVariableAbbr()
    {
        super.setText("VariableAbbr");
    }

    public TVariableAbbr(int line, int pos)
    {
        super.setText("VariableAbbr");
        setLine(line);
        setPos(pos);
    }

    public Object clone()
    {
      return new TVariableAbbr(getLine(), getPos());
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseTVariableAbbr(this);
    }

    public void setText(String text)
    {
        throw new RuntimeException("Cannot change TVariableAbbr text.");
    }
}