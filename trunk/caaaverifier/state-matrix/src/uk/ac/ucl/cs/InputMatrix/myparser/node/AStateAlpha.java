/* This file was generated by SableCC (http://www.sablecc.org/). */

package uk.ac.ucl.cs.InputMatrix.myparser.node;

import java.util.*;
import uk.ac.ucl.cs.InputMatrix.myparser.analysis.*;

public final class AStateAlpha extends PStateAlpha
{
    private PFactor _factor_;

    public AStateAlpha()
    {
    }

    public AStateAlpha(
        PFactor _factor_)
    {
        setFactor(_factor_);

    }
    public Object clone()
    {
        return new AStateAlpha(
            (PFactor) cloneNode(_factor_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAStateAlpha(this);
    }

    public PFactor getFactor()
    {
        return _factor_;
    }

    public void setFactor(PFactor node)
    {
        if(_factor_ != null)
        {
            _factor_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _factor_ = node;
    }

    public String toString()
    {
        return ""
            + toString(_factor_);
    }

    void removeChild(Node child)
    {
        if(_factor_ == child)
        {
            _factor_ = null;
            return;
        }

    }

    void replaceChild(Node oldChild, Node newChild)
    {
        if(_factor_ == oldChild)
        {
            setFactor((PFactor) newChild);
            return;
        }

    }
}
