/* This file was generated by SableCC (http://www.sablecc.org/). */

package uk.ac.ucl.cs.InputMatrix.myparser.node;

import java.util.*;
import uk.ac.ucl.cs.InputMatrix.myparser.analysis.*;

public final class AMyConstraintPair extends PMyConstraintPair
{
    private TLPar _lPar_;
    private PConditionPredicate _conditionPredicate_;
    private TComma _comma_;
    private PEffectPredicate _effectPredicate_;
    private TRPar _rPar_;

    public AMyConstraintPair()
    {
    }

    public AMyConstraintPair(
        TLPar _lPar_,
        PConditionPredicate _conditionPredicate_,
        TComma _comma_,
        PEffectPredicate _effectPredicate_,
        TRPar _rPar_)
    {
        setLPar(_lPar_);

        setConditionPredicate(_conditionPredicate_);

        setComma(_comma_);

        setEffectPredicate(_effectPredicate_);

        setRPar(_rPar_);

    }
    public Object clone()
    {
        return new AMyConstraintPair(
            (TLPar) cloneNode(_lPar_),
            (PConditionPredicate) cloneNode(_conditionPredicate_),
            (TComma) cloneNode(_comma_),
            (PEffectPredicate) cloneNode(_effectPredicate_),
            (TRPar) cloneNode(_rPar_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAMyConstraintPair(this);
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

    public PConditionPredicate getConditionPredicate()
    {
        return _conditionPredicate_;
    }

    public void setConditionPredicate(PConditionPredicate node)
    {
        if(_conditionPredicate_ != null)
        {
            _conditionPredicate_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _conditionPredicate_ = node;
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

    public PEffectPredicate getEffectPredicate()
    {
        return _effectPredicate_;
    }

    public void setEffectPredicate(PEffectPredicate node)
    {
        if(_effectPredicate_ != null)
        {
            _effectPredicate_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _effectPredicate_ = node;
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
            + toString(_conditionPredicate_)
            + toString(_comma_)
            + toString(_effectPredicate_)
            + toString(_rPar_);
    }

    void removeChild(Node child)
    {
        if(_lPar_ == child)
        {
            _lPar_ = null;
            return;
        }

        if(_conditionPredicate_ == child)
        {
            _conditionPredicate_ = null;
            return;
        }

        if(_comma_ == child)
        {
            _comma_ = null;
            return;
        }

        if(_effectPredicate_ == child)
        {
            _effectPredicate_ = null;
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

        if(_conditionPredicate_ == oldChild)
        {
            setConditionPredicate((PConditionPredicate) newChild);
            return;
        }

        if(_comma_ == oldChild)
        {
            setComma((TComma) newChild);
            return;
        }

        if(_effectPredicate_ == oldChild)
        {
            setEffectPredicate((PEffectPredicate) newChild);
            return;
        }

        if(_rPar_ == oldChild)
        {
            setRPar((TRPar) newChild);
            return;
        }

    }
}