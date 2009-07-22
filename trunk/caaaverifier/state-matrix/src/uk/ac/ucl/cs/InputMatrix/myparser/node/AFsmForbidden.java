/* This file was generated by SableCC (http://www.sablecc.org/). */

package uk.ac.ucl.cs.InputMatrix.myparser.node;

import java.util.*;
import uk.ac.ucl.cs.InputMatrix.myparser.analysis.*;

public final class AFsmForbidden extends PFsmForbidden
{
    private TForbidden _forbidden_;
    private TEqual _equal_;
    private TLBrace _lBrace_;
    private PForbiddenStateList _forbiddenStateList_;
    private TRBrace _rBrace_;

    public AFsmForbidden()
    {
    }

    public AFsmForbidden(
        TForbidden _forbidden_,
        TEqual _equal_,
        TLBrace _lBrace_,
        PForbiddenStateList _forbiddenStateList_,
        TRBrace _rBrace_)
    {
        setForbidden(_forbidden_);

        setEqual(_equal_);

        setLBrace(_lBrace_);

        setForbiddenStateList(_forbiddenStateList_);

        setRBrace(_rBrace_);

    }
    public Object clone()
    {
        return new AFsmForbidden(
            (TForbidden) cloneNode(_forbidden_),
            (TEqual) cloneNode(_equal_),
            (TLBrace) cloneNode(_lBrace_),
            (PForbiddenStateList) cloneNode(_forbiddenStateList_),
            (TRBrace) cloneNode(_rBrace_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAFsmForbidden(this);
    }

    public TForbidden getForbidden()
    {
        return _forbidden_;
    }

    public void setForbidden(TForbidden node)
    {
        if(_forbidden_ != null)
        {
            _forbidden_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _forbidden_ = node;
    }

    public TEqual getEqual()
    {
        return _equal_;
    }

    public void setEqual(TEqual node)
    {
        if(_equal_ != null)
        {
            _equal_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _equal_ = node;
    }

    public TLBrace getLBrace()
    {
        return _lBrace_;
    }

    public void setLBrace(TLBrace node)
    {
        if(_lBrace_ != null)
        {
            _lBrace_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _lBrace_ = node;
    }

    public PForbiddenStateList getForbiddenStateList()
    {
        return _forbiddenStateList_;
    }

    public void setForbiddenStateList(PForbiddenStateList node)
    {
        if(_forbiddenStateList_ != null)
        {
            _forbiddenStateList_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _forbiddenStateList_ = node;
    }

    public TRBrace getRBrace()
    {
        return _rBrace_;
    }

    public void setRBrace(TRBrace node)
    {
        if(_rBrace_ != null)
        {
            _rBrace_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _rBrace_ = node;
    }

    public String toString()
    {
        return ""
            + toString(_forbidden_)
            + toString(_equal_)
            + toString(_lBrace_)
            + toString(_forbiddenStateList_)
            + toString(_rBrace_);
    }

    void removeChild(Node child)
    {
        if(_forbidden_ == child)
        {
            _forbidden_ = null;
            return;
        }

        if(_equal_ == child)
        {
            _equal_ = null;
            return;
        }

        if(_lBrace_ == child)
        {
            _lBrace_ = null;
            return;
        }

        if(_forbiddenStateList_ == child)
        {
            _forbiddenStateList_ = null;
            return;
        }

        if(_rBrace_ == child)
        {
            _rBrace_ = null;
            return;
        }

    }

    void replaceChild(Node oldChild, Node newChild)
    {
        if(_forbidden_ == oldChild)
        {
            setForbidden((TForbidden) newChild);
            return;
        }

        if(_equal_ == oldChild)
        {
            setEqual((TEqual) newChild);
            return;
        }

        if(_lBrace_ == oldChild)
        {
            setLBrace((TLBrace) newChild);
            return;
        }

        if(_forbiddenStateList_ == oldChild)
        {
            setForbiddenStateList((PForbiddenStateList) newChild);
            return;
        }

        if(_rBrace_ == oldChild)
        {
            setRBrace((TRBrace) newChild);
            return;
        }

    }
}