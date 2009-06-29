/* This file was generated by SableCC (http://www.sablecc.org/). */

package uk.ac.ucl.cs.InputMatrix.myparser.node;

import java.util.*;
import uk.ac.ucl.cs.InputMatrix.myparser.analysis.*;

public final class AMyPredicate extends PMyPredicate
{
    private TLPar _lPar_;
    private PPredicateName _predicateName_;
    private TComma _comma_;
    private PPredicateLogic _predicateLogic_;
    private TRPar _rPar_;

    public AMyPredicate()
    {
    }

    public AMyPredicate(
        TLPar _lPar_,
        PPredicateName _predicateName_,
        TComma _comma_,
        PPredicateLogic _predicateLogic_,
        TRPar _rPar_)
    {
        setLPar(_lPar_);

        setPredicateName(_predicateName_);

        setComma(_comma_);

        setPredicateLogic(_predicateLogic_);

        setRPar(_rPar_);

    }
    public Object clone()
    {
        return new AMyPredicate(
            (TLPar) cloneNode(_lPar_),
            (PPredicateName) cloneNode(_predicateName_),
            (TComma) cloneNode(_comma_),
            (PPredicateLogic) cloneNode(_predicateLogic_),
            (TRPar) cloneNode(_rPar_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAMyPredicate(this);
    }

    public TLPar getLPar()
    {
        return _lPar_;
    }

    public void setLPar(TLPar node)
    {
        if(_lPar_ != null)
        {
            _lPar_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _lPar_ = node;
    }

    public PPredicateName getPredicateName()
    {
        return _predicateName_;
    }

    public void setPredicateName(PPredicateName node)
    {
        if(_predicateName_ != null)
        {
            _predicateName_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _predicateName_ = node;
    }

    public TComma getComma()
    {
        return _comma_;
    }

    public void setComma(TComma node)
    {
        if(_comma_ != null)
        {
            _comma_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _comma_ = node;
    }

    public PPredicateLogic getPredicateLogic()
    {
        return _predicateLogic_;
    }

    public void setPredicateLogic(PPredicateLogic node)
    {
        if(_predicateLogic_ != null)
        {
            _predicateLogic_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _predicateLogic_ = node;
    }

    public TRPar getRPar()
    {
        return _rPar_;
    }

    public void setRPar(TRPar node)
    {
        if(_rPar_ != null)
        {
            _rPar_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _rPar_ = node;
    }

    public String toString()
    {
        return ""
            + toString(_lPar_)
            + toString(_predicateName_)
            + toString(_comma_)
            + toString(_predicateLogic_)
            + toString(_rPar_);
    }

    void removeChild(Node child)
    {
        if(_lPar_ == child)
        {
            _lPar_ = null;
            return;
        }

        if(_predicateName_ == child)
        {
            _predicateName_ = null;
            return;
        }

        if(_comma_ == child)
        {
            _comma_ = null;
            return;
        }

        if(_predicateLogic_ == child)
        {
            _predicateLogic_ = null;
            return;
        }

        if(_rPar_ == child)
        {
            _rPar_ = null;
            return;
        }

    }

    void replaceChild(Node oldChild, Node newChild)
    {
        if(_lPar_ == oldChild)
        {
            setLPar((TLPar) newChild);
            return;
        }

        if(_predicateName_ == oldChild)
        {
            setPredicateName((PPredicateName) newChild);
            return;
        }

        if(_comma_ == oldChild)
        {
            setComma((TComma) newChild);
            return;
        }

        if(_predicateLogic_ == oldChild)
        {
            setPredicateLogic((PPredicateLogic) newChild);
            return;
        }

        if(_rPar_ == oldChild)
        {
            setRPar((TRPar) newChild);
            return;
        }

    }
}
