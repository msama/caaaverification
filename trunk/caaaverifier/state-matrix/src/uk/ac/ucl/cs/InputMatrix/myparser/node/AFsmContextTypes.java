/* This file was generated by SableCC (http://www.sablecc.org/). */

package uk.ac.ucl.cs.InputMatrix.myparser.node;

import java.util.*;
import uk.ac.ucl.cs.InputMatrix.myparser.analysis.*;

public final class AFsmContextTypes extends PFsmContextTypes
{
    private TContextTypes _contextTypes_;
    private TEqual _equal_;
    private PTypeList _typeList_;

    public AFsmContextTypes()
    {
    }

    public AFsmContextTypes(
        TContextTypes _contextTypes_,
        TEqual _equal_,
        PTypeList _typeList_)
    {
        setContextTypes(_contextTypes_);

        setEqual(_equal_);

        setTypeList(_typeList_);

    }
    public Object clone()
    {
        return new AFsmContextTypes(
            (TContextTypes) cloneNode(_contextTypes_),
            (TEqual) cloneNode(_equal_),
            (PTypeList) cloneNode(_typeList_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAFsmContextTypes(this);
    }

    public TContextTypes getContextTypes()
    {
        return _contextTypes_;
    }

    public void setContextTypes(TContextTypes node)
    {
        if(_contextTypes_ != null)
        {
            _contextTypes_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _contextTypes_ = node;
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

    public PTypeList getTypeList()
    {
        return _typeList_;
    }

    public void setTypeList(PTypeList node)
    {
        if(_typeList_ != null)
        {
            _typeList_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _typeList_ = node;
    }

    public String toString()
    {
        return ""
            + toString(_contextTypes_)
            + toString(_equal_)
            + toString(_typeList_);
    }

    void removeChild(Node child)
    {
        if(_contextTypes_ == child)
        {
            _contextTypes_ = null;
            return;
        }

        if(_equal_ == child)
        {
            _equal_ = null;
            return;
        }

        if(_typeList_ == child)
        {
            _typeList_ = null;
            return;
        }

    }

    void replaceChild(Node oldChild, Node newChild)
    {
        if(_contextTypes_ == oldChild)
        {
            setContextTypes((TContextTypes) newChild);
            return;
        }

        if(_equal_ == oldChild)
        {
            setEqual((TEqual) newChild);
            return;
        }

        if(_typeList_ == oldChild)
        {
            setTypeList((PTypeList) newChild);
            return;
        }

    }
}
