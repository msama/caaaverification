/* This file was generated by SableCC (http://www.sablecc.org/). */

package uk.ac.ucl.cs.InputMatrix.myparser.node;

import java.util.*;
import uk.ac.ucl.cs.InputMatrix.myparser.analysis.*;

public final class ASingleTriggers extends PTriggers
{
    private PAtrigger _atrigger_;

    public ASingleTriggers()
    {
    }

    public ASingleTriggers(
        PAtrigger _atrigger_)
    {
        setAtrigger(_atrigger_);

    }
    public Object clone()
    {
        return new ASingleTriggers(
            (PAtrigger) cloneNode(_atrigger_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseASingleTriggers(this);
    }

    public PAtrigger getAtrigger()
    {
        return _atrigger_;
    }

    public void setAtrigger(PAtrigger node)
    {
        if(_atrigger_ != null)
        {
            _atrigger_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _atrigger_ = node;
    }

    public String toString()
    {
        return ""
            + toString(_atrigger_);
    }

    void removeChild(Node child)
    {
        if(_atrigger_ == child)
        {
            _atrigger_ = null;
            return;
        }

    }

    void replaceChild(Node oldChild, Node newChild)
    {
        if(_atrigger_ == oldChild)
        {
            setAtrigger((PAtrigger) newChild);
            return;
        }

    }
}
