/* This file was generated by SableCC (http://www.sablecc.org/). */

package uk.ac.ucl.cs.InputMatrix.myparser.node;

import java.util.*;
import uk.ac.ucl.cs.InputMatrix.myparser.analysis.*;

public final class AFsmPredicateAbbr extends PFsmPredicateAbbr
{
    private TPredicateAbbr _predicateAbbr_;
    private TEqual _equal_;
    private PPredicateList _predicateList_;

    public AFsmPredicateAbbr()
    {
    }

    public AFsmPredicateAbbr(
        TPredicateAbbr _predicateAbbr_,
        TEqual _equal_,
        PPredicateList _predicateList_)
    {
        setPredicateAbbr(_predicateAbbr_);

        setEqual(_equal_);

        setPredicateList(_predicateList_);

    }
    public Object clone()
    {
        return new AFsmPredicateAbbr(
            (TPredicateAbbr) cloneNode(_predicateAbbr_),
            (TEqual) cloneNode(_equal_),
            (PPredicateList) cloneNode(_predicateList_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAFsmPredicateAbbr(this);
    }

    public TPredicateAbbr getPredicateAbbr()
    {
        return _predicateAbbr_;
    }

    public void setPredicateAbbr(TPredicateAbbr node)
    {
        if(_predicateAbbr_ != null)
        {
            _predicateAbbr_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _predicateAbbr_ = node;
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

    public PPredicateList getPredicateList()
    {
        return _predicateList_;
    }

    public void setPredicateList(PPredicateList node)
    {
        if(_predicateList_ != null)
        {
            _predicateList_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _predicateList_ = node;
    }

    public String toString()
    {
        return ""
            + toString(_predicateAbbr_)
            + toString(_equal_)
            + toString(_predicateList_);
    }

    void removeChild(Node child)
    {
        if(_predicateAbbr_ == child)
        {
            _predicateAbbr_ = null;
            return;
        }

        if(_equal_ == child)
        {
            _equal_ = null;
            return;
        }

        if(_predicateList_ == child)
        {
            _predicateList_ = null;
            return;
        }

    }

    void replaceChild(Node oldChild, Node newChild)
    {
        if(_predicateAbbr_ == oldChild)
        {
            setPredicateAbbr((TPredicateAbbr) newChild);
            return;
        }

        if(_equal_ == oldChild)
        {
            setEqual((TEqual) newChild);
            return;
        }

        if(_predicateList_ == oldChild)
        {
            setPredicateList((PPredicateList) newChild);
            return;
        }

    }
}
