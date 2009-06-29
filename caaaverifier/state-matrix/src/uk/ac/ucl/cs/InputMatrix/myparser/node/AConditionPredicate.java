/* This file was generated by SableCC (http://www.sablecc.org/). */

package uk.ac.ucl.cs.InputMatrix.myparser.node;

import java.util.*;
import uk.ac.ucl.cs.InputMatrix.myparser.analysis.*;

public final class AConditionPredicate extends PConditionPredicate
{
    private PPredicateLogic _predicateLogic_;

    public AConditionPredicate()
    {
    }

    public AConditionPredicate(
        PPredicateLogic _predicateLogic_)
    {
        setPredicateLogic(_predicateLogic_);

    }
    public Object clone()
    {
        return new AConditionPredicate(
            (PPredicateLogic) cloneNode(_predicateLogic_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAConditionPredicate(this);
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

    public String toString()
    {
        return ""
            + toString(_predicateLogic_);
    }

    void removeChild(Node child)
    {
        if(_predicateLogic_ == child)
        {
            _predicateLogic_ = null;
            return;
        }

    }

    void replaceChild(Node oldChild, Node newChild)
    {
        if(_predicateLogic_ == oldChild)
        {
            setPredicateLogic((PPredicateLogic) newChild);
            return;
        }

    }
}
