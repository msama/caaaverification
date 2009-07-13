/* This file was generated by SableCC (http://www.sablecc.org/). */

package uk.ac.ucl.cs.InputMatrix.myparser.node;

import java.util.*;
import uk.ac.ucl.cs.InputMatrix.myparser.analysis.*;

public final class AForbiddenStateList extends PForbiddenStateList
{
    private PStateList _stateList_;

    public AForbiddenStateList()
    {
    }

    public AForbiddenStateList(
        PStateList _stateList_)
    {
        setStateList(_stateList_);

    }
    public Object clone()
    {
        return new AForbiddenStateList(
            (PStateList) cloneNode(_stateList_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAForbiddenStateList(this);
    }

    public PStateList getStateList()
    {
        return _stateList_;
    }

    public void setStateList(PStateList node)
    {
        if(_stateList_ != null)
        {
            _stateList_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _stateList_ = node;
    }

    public String toString()
    {
        return ""
            + toString(_stateList_);
    }

    void removeChild(Node child)
    {
        if(_stateList_ == child)
        {
            _stateList_ = null;
            return;
        }

    }

    void replaceChild(Node oldChild, Node newChild)
    {
        if(_stateList_ == oldChild)
        {
            setStateList((PStateList) newChild);
            return;
        }

    }
}