/* This file was generated by SableCC (http://www.sablecc.org/). */

package uk.ac.ucl.cs.InputMatrix.myparser.node;

import uk.ac.ucl.cs.InputMatrix.myparser.analysis.*;

public final class TLAnd extends Token
{
    public TLAnd()
    {
        super.setText("\\AND");
    }

    public TLAnd(int line, int pos)
    {
        super.setText("\\AND");
        setLine(line);
        setPos(pos);
    }

    public Object clone()
    {
      return new TLAnd(getLine(), getPos());
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseTLAnd(this);
    }

    public void setText(String text)
    {
        throw new RuntimeException("Cannot change TLAnd text.");
    }
}