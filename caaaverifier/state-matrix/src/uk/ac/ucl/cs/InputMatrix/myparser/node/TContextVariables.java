/* This file was generated by SableCC (http://www.sablecc.org/). */

package uk.ac.ucl.cs.InputMatrix.myparser.node;

import uk.ac.ucl.cs.InputMatrix.myparser.analysis.*;

public final class TContextVariables extends Token
{
    public TContextVariables()
    {
        super.setText("ContextVariables");
    }

    public TContextVariables(int line, int pos)
    {
        super.setText("ContextVariables");
        setLine(line);
        setPos(pos);
    }

    public Object clone()
    {
      return new TContextVariables(getLine(), getPos());
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseTContextVariables(this);
    }

    public void setText(String text)
    {
        throw new RuntimeException("Cannot change TContextVariables text.");
    }
}
