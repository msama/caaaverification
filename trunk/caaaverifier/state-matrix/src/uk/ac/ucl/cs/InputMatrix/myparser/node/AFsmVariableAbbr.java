/* This file was generated by SableCC (http://www.sablecc.org/). */

package uk.ac.ucl.cs.InputMatrix.myparser.node;

import java.util.*;
import uk.ac.ucl.cs.InputMatrix.myparser.analysis.*;

public final class AFsmVariableAbbr extends PFsmVariableAbbr
{
    private TVariableAbbr _variableAbbr_;
    private TEqual _equal_;
    private PAbbrVariableList _abbrVariableList_;

    public AFsmVariableAbbr()
    {
    }

    public AFsmVariableAbbr(
        TVariableAbbr _variableAbbr_,
        TEqual _equal_,
        PAbbrVariableList _abbrVariableList_)
    {
        setVariableAbbr(_variableAbbr_);

        setEqual(_equal_);

        setAbbrVariableList(_abbrVariableList_);

    }
    public Object clone()
    {
        return new AFsmVariableAbbr(
            (TVariableAbbr) cloneNode(_variableAbbr_),
            (TEqual) cloneNode(_equal_),
            (PAbbrVariableList) cloneNode(_abbrVariableList_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAFsmVariableAbbr(this);
    }

    public TVariableAbbr getVariableAbbr()
    {
        return _variableAbbr_;
    }

    public void setVariableAbbr(TVariableAbbr node)
    {
        if(_variableAbbr_ != null)
        {
            _variableAbbr_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _variableAbbr_ = node;
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

    public PAbbrVariableList getAbbrVariableList()
    {
        return _abbrVariableList_;
    }

    public void setAbbrVariableList(PAbbrVariableList node)
    {
        if(_abbrVariableList_ != null)
        {
            _abbrVariableList_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _abbrVariableList_ = node;
    }

    public String toString()
    {
        return ""
            + toString(_variableAbbr_)
            + toString(_equal_)
            + toString(_abbrVariableList_);
    }

    void removeChild(Node child)
    {
        if(_variableAbbr_ == child)
        {
            _variableAbbr_ = null;
            return;
        }

        if(_equal_ == child)
        {
            _equal_ = null;
            return;
        }

        if(_abbrVariableList_ == child)
        {
            _abbrVariableList_ = null;
            return;
        }

    }

    void replaceChild(Node oldChild, Node newChild)
    {
        if(_variableAbbr_ == oldChild)
        {
            setVariableAbbr((TVariableAbbr) newChild);
            return;
        }

        if(_equal_ == oldChild)
        {
            setEqual((TEqual) newChild);
            return;
        }

        if(_abbrVariableList_ == oldChild)
        {
            setAbbrVariableList((PAbbrVariableList) newChild);
            return;
        }

    }
}