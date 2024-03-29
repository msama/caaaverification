/* This file was generated by SableCC (http://www.sablecc.org/). */

package uk.ac.ucl.cs.InputMatrix.myparser.node;

import java.util.*;
import uk.ac.ucl.cs.InputMatrix.myparser.analysis.*;

public final class ASingleStateList extends PStateList
{
    private PStateAlpha _stateAlpha_;

    public ASingleStateList()
    {
    }

    public ASingleStateList(
        PStateAlpha _stateAlpha_)
    {
        setStateAlpha(_stateAlpha_);

    }
    public Object clone()
    {
        return new ASingleStateList(
            (PStateAlpha) cloneNode(_stateAlpha_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseASingleStateList(this);
    }

    public PStateAlpha getStateAlpha()
    {
        return _stateAlpha_;
    }

    public void setStateAlpha(PStateAlpha node)
    {
        if(_stateAlpha_ != null)
        {
            _stateAlpha_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _stateAlpha_ = node;
    }

    public String toString()
    {
        return ""
            + toString(_stateAlpha_);
    }

    void removeChild(Node child)
    {
        if(_stateAlpha_ == child)
        {
            _stateAlpha_ = null;
            return;
        }

    }

    void replaceChild(Node oldChild, Node newChild)
    {
        if(_stateAlpha_ == oldChild)
        {
            setStateAlpha((PStateAlpha) newChild);
            return;
        }

    }
}
