/* This file was generated by SableCC (http://www.sablecc.org/). */

package uk.ac.ucl.cs.InputMatrix.myparser.node;

import uk.ac.ucl.cs.InputMatrix.myparser.analysis.*;

public final class TLIn extends Token
{
    public TLIn()
    {
        super.setText("\\IN");
    }

    public TLIn(int line, int pos)
    {
        super.setText("\\IN");
        setLine(line);
        setPos(pos);
    }

    public Object clone()
    {
      return new TLIn(getLine(), getPos());
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseTLIn(this);
    }

    public void setText(String text)
    {
        throw new RuntimeException("Cannot change TLIn text.");
    }
}
