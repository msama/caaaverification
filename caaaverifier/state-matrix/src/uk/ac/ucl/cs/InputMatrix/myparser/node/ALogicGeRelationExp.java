/* This file was generated by SableCC (http://www.sablecc.org/). */

package uk.ac.ucl.cs.InputMatrix.myparser.node;

import java.util.*;
import uk.ac.ucl.cs.InputMatrix.myparser.analysis.*;

public final class ALogicGeRelationExp extends PRelationExp
{
    private PMyUnaryExp _ge1_;
    private TLGe _lGe_;
    private PMyUnaryExp _ge2_;

    public ALogicGeRelationExp()
    {
    }

    public ALogicGeRelationExp(
        PMyUnaryExp _ge1_,
        TLGe _lGe_,
        PMyUnaryExp _ge2_)
    {
        setGe1(_ge1_);

        setLGe(_lGe_);

        setGe2(_ge2_);

    }
    public Object clone()
    {
        return new ALogicGeRelationExp(
            (PMyUnaryExp) cloneNode(_ge1_),
            (TLGe) cloneNode(_lGe_),
            (PMyUnaryExp) cloneNode(_ge2_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseALogicGeRelationExp(this);
    }

    public PMyUnaryExp getGe1()
    {
        return _ge1_;
    }

    public void setGe1(PMyUnaryExp node)
    {
        if(_ge1_ != null)
        {
            _ge1_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _ge1_ = node;
    }

    public TLGe getLGe()
    {
        return _lGe_;
    }

    public void setLGe(TLGe node)
    {
        if(_lGe_ != null)
        {
            _lGe_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _lGe_ = node;
    }

    public PMyUnaryExp getGe2()
    {
        return _ge2_;
    }

    public void setGe2(PMyUnaryExp node)
    {
        if(_ge2_ != null)
        {
            _ge2_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _ge2_ = node;
    }

    public String toString()
    {
        return ""
            + toString(_ge1_)
            + toString(_lGe_)
            + toString(_ge2_);
    }

    void removeChild(Node child)
    {
        if(_ge1_ == child)
        {
            _ge1_ = null;
            return;
        }

        if(_lGe_ == child)
        {
            _lGe_ = null;
            return;
        }

        if(_ge2_ == child)
        {
            _ge2_ = null;
            return;
        }

    }

    void replaceChild(Node oldChild, Node newChild)
    {
        if(_ge1_ == oldChild)
        {
            setGe1((PMyUnaryExp) newChild);
            return;
        }

        if(_lGe_ == oldChild)
        {
            setLGe((TLGe) newChild);
            return;
        }

        if(_ge2_ == oldChild)
        {
            setGe2((PMyUnaryExp) newChild);
            return;
        }

    }
}
