/* This file was generated by SableCC (http://www.sablecc.org/). */

package uk.ac.ucl.cs.InputMatrix.myparser.node;

import uk.ac.ucl.cs.InputMatrix.myparser.analysis.*;

public final class TLLe extends Token
{
    public TLLe()
    {
        super.setText("\\LE");
    }

    public TLLe(int line, int pos)
    {
        super.setText("\\LE");
        setLine(line);
        setPos(pos);
    }

    public Object clone()
    {
      return new TLLe(getLine(), getPos());
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseTLLe(this);
    }

    public void setText(String text)
    {
        throw new RuntimeException("Cannot change TLLe text.");
    }
}
