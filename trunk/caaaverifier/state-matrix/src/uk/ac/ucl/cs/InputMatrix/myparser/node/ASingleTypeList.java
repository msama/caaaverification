/* This file was generated by SableCC (http://www.sablecc.org/). */

package uk.ac.ucl.cs.InputMatrix.myparser.node;

import java.util.*;
import uk.ac.ucl.cs.InputMatrix.myparser.analysis.*;

public final class ASingleTypeList extends PTypeList
{
    private PMyType _myType_;

    public ASingleTypeList()
    {
    }

    public ASingleTypeList(
        PMyType _myType_)
    {
        setMyType(_myType_);

    }
    public Object clone()
    {
        return new ASingleTypeList(
            (PMyType) cloneNode(_myType_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseASingleTypeList(this);
    }

    public PMyType getMyType()
    {
        return _myType_;
    }

    public void setMyType(PMyType node)
    {
        if(_myType_ != null)
        {
            _myType_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _myType_ = node;
    }

    public String toString()
    {
        return ""
            + toString(_myType_);
    }

    void removeChild(Node child)
    {
        if(_myType_ == child)
        {
            _myType_ = null;
            return;
        }

    }

    void replaceChild(Node oldChild, Node newChild)
    {
        if(_myType_ == oldChild)
        {
            setMyType((PMyType) newChild);
            return;
        }

    }
}
