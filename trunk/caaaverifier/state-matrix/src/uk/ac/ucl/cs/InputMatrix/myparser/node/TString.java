/* This file was generated by SableCC (http://www.sablecc.org/). */

package uk.ac.ucl.cs.InputMatrix.myparser.node;

import uk.ac.ucl.cs.InputMatrix.myparser.analysis.*;

public final class TString extends Token
{
    public TString(String text)
    {
        setText(text);
    }

    public TString(String text, int line, int pos)
    {
        setText(text);
        setLine(line);
        setPos(pos);
    }

    public Object clone()
    {
      return new TString(getText(), getLine(), getPos());
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseTString(this);
    }
}