/* This file was generated by SableCC (http://www.sablecc.org/). */

package uk.ac.ucl.cs.InputMatrix.myparser.node;

import java.util.*;
import uk.ac.ucl.cs.InputMatrix.myparser.analysis.*;

public final class ASingleAbbrVariableList extends PAbbrVariableList
{
    private POneVariablePair _oneVariablePair_;

    public ASingleAbbrVariableList()
    {
    }

    public ASingleAbbrVariableList(
        POneVariablePair _oneVariablePair_)
    {
        setOneVariablePair(_oneVariablePair_);

    }
    public Object clone()
    {
        return new ASingleAbbrVariableList(
            (POneVariablePair) cloneNode(_oneVariablePair_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseASingleAbbrVariableList(this);
    }

    public POneVariablePair getOneVariablePair()
    {
        return _oneVariablePair_;
    }

    public void setOneVariablePair(POneVariablePair node)
    {
        if(_oneVariablePair_ != null)
        {
            _oneVariablePair_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _oneVariablePair_ = node;
    }

    public String toString()
    {
        return ""
            + toString(_oneVariablePair_);
    }

    void removeChild(Node child)
    {
        if(_oneVariablePair_ == child)
        {
            _oneVariablePair_ = null;
            return;
        }

    }

    void replaceChild(Node oldChild, Node newChild)
    {
        if(_oneVariablePair_ == oldChild)
        {
            setOneVariablePair((POneVariablePair) newChild);
            return;
        }

    }
}