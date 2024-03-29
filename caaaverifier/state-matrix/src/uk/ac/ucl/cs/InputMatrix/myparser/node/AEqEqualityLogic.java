/* This file was generated by SableCC (http://www.sablecc.org/). */

package uk.ac.ucl.cs.InputMatrix.myparser.node;

import java.util.*;
import uk.ac.ucl.cs.InputMatrix.myparser.analysis.*;

public final class AEqEqualityLogic extends PEqualityLogic
{
    private TLEq _lEq_;

    public AEqEqualityLogic()
    {
    }

    public AEqEqualityLogic(
        TLEq _lEq_)
    {
        setLEq(_lEq_);

    }
    public Object clone()
    {
        return new AEqEqualityLogic(
            (TLEq) cloneNode(_lEq_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAEqEqualityLogic(this);
    }

    public TLEq getLEq()
    {
        return _lEq_;
    }

    public void setLEq(TLEq node)
    {
        if(_lEq_ != null)
        {
            _lEq_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _lEq_ = node;
    }

    public String toString()
    {
        return ""
            + toString(_lEq_);
    }

    void removeChild(Node child)
    {
        if(_lEq_ == child)
        {
            _lEq_ = null;
            return;
        }

    }

    void replaceChild(Node oldChild, Node newChild)
    {
        if(_lEq_ == oldChild)
        {
            setLEq((TLEq) newChild);
            return;
        }

    }
}
