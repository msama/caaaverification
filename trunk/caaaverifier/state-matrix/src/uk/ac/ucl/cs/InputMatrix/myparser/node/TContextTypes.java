/* This file was generated by SableCC (http://www.sablecc.org/). */

package uk.ac.ucl.cs.InputMatrix.myparser.node;

import uk.ac.ucl.cs.InputMatrix.myparser.analysis.*;

public final class TContextTypes extends Token
{
    public TContextTypes()
    {
        super.setText("ContextTypes");
    }

    public TContextTypes(int line, int pos)
    {
        super.setText("ContextTypes");
        setLine(line);
        setPos(pos);
    }

    public Object clone()
    {
      return new TContextTypes(getLine(), getPos());
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseTContextTypes(this);
    }

    public void setText(String text)
    {
        throw new RuntimeException("Cannot change TContextTypes text.");
    }
}
