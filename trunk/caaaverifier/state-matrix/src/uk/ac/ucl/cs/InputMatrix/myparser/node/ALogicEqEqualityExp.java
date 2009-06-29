/* This file was generated by SableCC (http://www.sablecc.org/). */

package uk.ac.ucl.cs.InputMatrix.myparser.node;

import java.util.*;
import uk.ac.ucl.cs.InputMatrix.myparser.analysis.*;

public final class ALogicEqEqualityExp extends PEqualityExp
{
    private PRelationExp _eq1_;
    private TLEq _lEq_;
    private PRelationExp _eq2_;

    public ALogicEqEqualityExp()
    {
    }

    public ALogicEqEqualityExp(
        PRelationExp _eq1_,
        TLEq _lEq_,
        PRelationExp _eq2_)
    {
        setEq1(_eq1_);

        setLEq(_lEq_);

        setEq2(_eq2_);

    }
    public Object clone()
    {
        return new ALogicEqEqualityExp(
            (PRelationExp) cloneNode(_eq1_),
            (TLEq) cloneNode(_lEq_),
            (PRelationExp) cloneNode(_eq2_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseALogicEqEqualityExp(this);
    }

    public PRelationExp getEq1()
    {
        return _eq1_;
    }

    public void setEq1(PRelationExp node)
    {
        if(_eq1_ != null)
        {
            _eq1_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _eq1_ = node;
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

    public PRelationExp getEq2()
    {
        return _eq2_;
    }

    public void setEq2(PRelationExp node)
    {
        if(_eq2_ != null)
        {
            _eq2_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _eq2_ = node;
    }

    public String toString()
    {
        return ""
            + toString(_eq1_)
            + toString(_lEq_)
            + toString(_eq2_);
    }

    void removeChild(Node child)
    {
        if(_eq1_ == child)
        {
            _eq1_ = null;
            return;
        }

        if(_lEq_ == child)
        {
            _lEq_ = null;
            return;
        }

        if(_eq2_ == child)
        {
            _eq2_ = null;
            return;
        }

    }

    void replaceChild(Node oldChild, Node newChild)
    {
        if(_eq1_ == oldChild)
        {
            setEq1((PRelationExp) newChild);
            return;
        }

        if(_lEq_ == oldChild)
        {
            setLEq((TLEq) newChild);
            return;
        }

        if(_eq2_ == oldChild)
        {
            setEq2((PRelationExp) newChild);
            return;
        }

    }
}
