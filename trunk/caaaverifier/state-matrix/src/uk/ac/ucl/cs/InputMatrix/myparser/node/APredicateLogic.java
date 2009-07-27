/* This file was generated by SableCC (http://www.sablecc.org/). */

package uk.ac.ucl.cs.InputMatrix.myparser.node;

import java.util.*;
import uk.ac.ucl.cs.InputMatrix.myparser.analysis.*;

public final class APredicateLogic extends PPredicateLogic
{
    private PLogicOrExp _logicOrExp_;

    public APredicateLogic()
    {
    }

    public APredicateLogic(
        PLogicOrExp _logicOrExp_)
    {
        setLogicOrExp(_logicOrExp_);

    }
    public Object clone()
    {
        return new APredicateLogic(
            (PLogicOrExp) cloneNode(_logicOrExp_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAPredicateLogic(this);
    }

    public PLogicOrExp getLogicOrExp()
    {
        return _logicOrExp_;
    }

    public void setLogicOrExp(PLogicOrExp node)
    {
        if(_logicOrExp_ != null)
        {
            _logicOrExp_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _logicOrExp_ = node;
    }

    public String toString()
    {
        return ""
            + toString(_logicOrExp_);
    }

    void removeChild(Node child)
    {
        if(_logicOrExp_ == child)
        {
            _logicOrExp_ = null;
            return;
        }

    }

    void replaceChild(Node oldChild, Node newChild)
    {
        if(_logicOrExp_ == oldChild)
        {
            setLogicOrExp((PLogicOrExp) newChild);
            return;
        }

    }
}