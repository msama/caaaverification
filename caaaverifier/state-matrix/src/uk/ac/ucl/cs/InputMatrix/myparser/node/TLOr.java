/* This file was generated by SableCC (http://www.sablecc.org/). */

package uk.ac.ucl.cs.InputMatrix.myparser.node;

import uk.ac.ucl.cs.InputMatrix.myparser.analysis.*;

public final class TLOr extends Token
{
    public TLOr()
    {
        super.setText("\\OR");
    }

    public TLOr(int line, int pos)
    {
        super.setText("\\OR");
        setLine(line);
        setPos(pos);
    }

    public Object clone()
    {
      return new TLOr(getLine(), getPos());
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseTLOr(this);
    }

    public void setText(String text)
    {
        throw new RuntimeException("Cannot change TLOr text.");
    }
}