/* This file was generated by SableCC (http://www.sablecc.org/). */

package uk.ac.ucl.cs.InputMatrix.myparser.node;

import java.util.*;
import uk.ac.ucl.cs.InputMatrix.myparser.analysis.*;

public final class AEmptyEqualityExp extends PEqualityExp
{
    private PRelationExp _relationExp_;

    public AEmptyEqualityExp()
    {
    }

    public AEmptyEqualityExp(
        PRelationExp _relationExp_)
    {
        setRelationExp(_relationExp_);

    }
    public Object clone()
    {
        return new AEmptyEqualityExp(
            (PRelationExp) cloneNode(_relationExp_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAEmptyEqualityExp(this);
    }

    public PRelationExp getRelationExp()
    {
        return _relationExp_;
    }

    public void setRelationExp(PRelationExp node)
    {
        if(_relationExp_ != null)
        {
            _relationExp_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _relationExp_ = node;
    }

    public String toString()
    {
        return ""
            + toString(_relationExp_);
    }

    void removeChild(Node child)
    {
        if(_relationExp_ == child)
        {
            _relationExp_ = null;
            return;
        }

    }

    void replaceChild(Node oldChild, Node newChild)
    {
        if(_relationExp_ == oldChild)
        {
            setRelationExp((PRelationExp) newChild);
            return;
        }

    }
}
