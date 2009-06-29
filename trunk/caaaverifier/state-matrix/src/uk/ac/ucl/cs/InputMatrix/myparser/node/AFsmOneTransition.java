/* This file was generated by SableCC (http://www.sablecc.org/). */

package uk.ac.ucl.cs.InputMatrix.myparser.node;

import java.util.*;
import uk.ac.ucl.cs.InputMatrix.myparser.analysis.*;

public final class AFsmOneTransition extends POneTransition
{
    private TLPar _l1_;
    private PMyStateAlpha1 _state1_;
    private TComma _c_;
    private PMyTransitionInputAlpha _alpha_;
    private TRPar _r1_;
    private TArrow _arrow_;
    private PMyStateAlpha2 _state2_;

    public AFsmOneTransition()
    {
    }

    public AFsmOneTransition(
        TLPar _l1_,
        PMyStateAlpha1 _state1_,
        TComma _c_,
        PMyTransitionInputAlpha _alpha_,
        TRPar _r1_,
        TArrow _arrow_,
        PMyStateAlpha2 _state2_)
    {
        setL1(_l1_);

        setState1(_state1_);

        setC(_c_);

        setAlpha(_alpha_);

        setR1(_r1_);

        setArrow(_arrow_);

        setState2(_state2_);

    }
    public Object clone()
    {
        return new AFsmOneTransition(
            (TLPar) cloneNode(_l1_),
            (PMyStateAlpha1) cloneNode(_state1_),
            (TComma) cloneNode(_c_),
            (PMyTransitionInputAlpha) cloneNode(_alpha_),
            (TRPar) cloneNode(_r1_),
            (TArrow) cloneNode(_arrow_),
            (PMyStateAlpha2) cloneNode(_state2_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAFsmOneTransition(this);
    }

    public TLPar getL1()
    {
        return _l1_;
    }

    public void setL1(TLPar node)
    {
        if(_l1_ != null)
        {
            _l1_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _l1_ = node;
    }

    public PMyStateAlpha1 getState1()
    {
        return _state1_;
    }

    public void setState1(PMyStateAlpha1 node)
    {
        if(_state1_ != null)
        {
            _state1_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _state1_ = node;
    }

    public TComma getC()
    {
        return _c_;
    }

    public void setC(TComma node)
    {
        if(_c_ != null)
        {
            _c_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _c_ = node;
    }

    public PMyTransitionInputAlpha getAlpha()
    {
        return _alpha_;
    }

    public void setAlpha(PMyTransitionInputAlpha node)
    {
        if(_alpha_ != null)
        {
            _alpha_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _alpha_ = node;
    }

    public TRPar getR1()
    {
        return _r1_;
    }

    public void setR1(TRPar node)
    {
        if(_r1_ != null)
        {
            _r1_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _r1_ = node;
    }

    public TArrow getArrow()
    {
        return _arrow_;
    }

    public void setArrow(TArrow node)
    {
        if(_arrow_ != null)
        {
            _arrow_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _arrow_ = node;
    }

    public PMyStateAlpha2 getState2()
    {
        return _state2_;
    }

    public void setState2(PMyStateAlpha2 node)
    {
        if(_state2_ != null)
        {
            _state2_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _state2_ = node;
    }

    public String toString()
    {
        return ""
            + toString(_l1_)
            + toString(_state1_)
            + toString(_c_)
            + toString(_alpha_)
            + toString(_r1_)
            + toString(_arrow_)
            + toString(_state2_);
    }

    void removeChild(Node child)
    {
        if(_l1_ == child)
        {
            _l1_ = null;
            return;
        }

        if(_state1_ == child)
        {
            _state1_ = null;
            return;
        }

        if(_c_ == child)
        {
            _c_ = null;
            return;
        }

        if(_alpha_ == child)
        {
            _alpha_ = null;
            return;
        }

        if(_r1_ == child)
        {
            _r1_ = null;
            return;
        }

        if(_arrow_ == child)
        {
            _arrow_ = null;
            return;
        }

        if(_state2_ == child)
        {
            _state2_ = null;
            return;
        }

    }

    void replaceChild(Node oldChild, Node newChild)
    {
        if(_l1_ == oldChild)
        {
            setL1((TLPar) newChild);
            return;
        }

        if(_state1_ == oldChild)
        {
            setState1((PMyStateAlpha1) newChild);
            return;
        }

        if(_c_ == oldChild)
        {
            setC((TComma) newChild);
            return;
        }

        if(_alpha_ == oldChild)
        {
            setAlpha((PMyTransitionInputAlpha) newChild);
            return;
        }

        if(_r1_ == oldChild)
        {
            setR1((TRPar) newChild);
            return;
        }

        if(_arrow_ == oldChild)
        {
            setArrow((TArrow) newChild);
            return;
        }

        if(_state2_ == oldChild)
        {
            setState2((PMyStateAlpha2) newChild);
            return;
        }

    }
}
