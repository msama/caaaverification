/* This file was generated by SableCC (http://www.sablecc.org/). */

package uk.ac.ucl.cs.InputMatrix.myparser.node;

import java.util.*;
import uk.ac.ucl.cs.InputMatrix.myparser.analysis.*;

public final class AFsmSpecs extends PSpecs
{
    private PFsmSpec _fsmSpec_;

    public AFsmSpecs()
    {
    }

    public AFsmSpecs(
        PFsmSpec _fsmSpec_)
    {
        setFsmSpec(_fsmSpec_);

    }
    public Object clone()
    {
        return new AFsmSpecs(
            (PFsmSpec) cloneNode(_fsmSpec_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAFsmSpecs(this);
    }

    public PFsmSpec getFsmSpec()
    {
        return _fsmSpec_;
    }

    public void setFsmSpec(PFsmSpec node)
    {
        if(_fsmSpec_ != null)
        {
            _fsmSpec_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _fsmSpec_ = node;
    }

    public String toString()
    {
        return ""
            + toString(_fsmSpec_);
    }

    void removeChild(Node child)
    {
        if(_fsmSpec_ == child)
        {
            _fsmSpec_ = null;
            return;
        }

    }

    void replaceChild(Node oldChild, Node newChild)
    {
        if(_fsmSpec_ == oldChild)
        {
            setFsmSpec((PFsmSpec) newChild);
            return;
        }

    }
}
