/* This file was generated by SableCC (http://www.sablecc.org/). */

package uk.ac.ucl.cs.InputMatrix.myparser.node;

import java.util.*;
import uk.ac.ucl.cs.InputMatrix.myparser.analysis.*;

public final class AFsmSpec extends PFsmSpec
{
    private PFsmName _fsmName_;
    private PFsmState _fsmState_;
    private PFsmInputAlpha _fsmInputAlpha_;
    private PFsmStart _fsmStart_;
    private PFsmForbidden _fsmForbidden_;
    private PFsmTransition _fsmTransition_;
    private PFsmTrigger _fsmTrigger_;
    private PFsmPriority _fsmPriority_;
    private PFsmContextTypes _fsmContextTypes_;
    private PFsmContextVariables _fsmContextVariables_;
    private PFsmPredicateAbbr _fsmPredicateAbbr_;
    private PFsmConstraintPair _fsmConstraintPair_;

    public AFsmSpec()
    {
    }

    public AFsmSpec(
        PFsmName _fsmName_,
        PFsmState _fsmState_,
        PFsmInputAlpha _fsmInputAlpha_,
        PFsmStart _fsmStart_,
        PFsmForbidden _fsmForbidden_,
        PFsmTransition _fsmTransition_,
        PFsmTrigger _fsmTrigger_,
        PFsmPriority _fsmPriority_,
        PFsmContextTypes _fsmContextTypes_,
        PFsmContextVariables _fsmContextVariables_,
        PFsmPredicateAbbr _fsmPredicateAbbr_,
        PFsmConstraintPair _fsmConstraintPair_)
    {
        setFsmName(_fsmName_);

        setFsmState(_fsmState_);

        setFsmInputAlpha(_fsmInputAlpha_);

        setFsmStart(_fsmStart_);

        setFsmForbidden(_fsmForbidden_);

        setFsmTransition(_fsmTransition_);

        setFsmTrigger(_fsmTrigger_);

        setFsmPriority(_fsmPriority_);

        setFsmContextTypes(_fsmContextTypes_);

        setFsmContextVariables(_fsmContextVariables_);

        setFsmPredicateAbbr(_fsmPredicateAbbr_);

        setFsmConstraintPair(_fsmConstraintPair_);

    }
    public Object clone()
    {
        return new AFsmSpec(
            (PFsmName) cloneNode(_fsmName_),
            (PFsmState) cloneNode(_fsmState_),
            (PFsmInputAlpha) cloneNode(_fsmInputAlpha_),
            (PFsmStart) cloneNode(_fsmStart_),
            (PFsmForbidden) cloneNode(_fsmForbidden_),
            (PFsmTransition) cloneNode(_fsmTransition_),
            (PFsmTrigger) cloneNode(_fsmTrigger_),
            (PFsmPriority) cloneNode(_fsmPriority_),
            (PFsmContextTypes) cloneNode(_fsmContextTypes_),
            (PFsmContextVariables) cloneNode(_fsmContextVariables_),
            (PFsmPredicateAbbr) cloneNode(_fsmPredicateAbbr_),
            (PFsmConstraintPair) cloneNode(_fsmConstraintPair_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAFsmSpec(this);
    }

    public PFsmName getFsmName()
    {
        return _fsmName_;
    }

    public void setFsmName(PFsmName node)
    {
        if(_fsmName_ != null)
        {
            _fsmName_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _fsmName_ = node;
    }

    public PFsmState getFsmState()
    {
        return _fsmState_;
    }

    public void setFsmState(PFsmState node)
    {
        if(_fsmState_ != null)
        {
            _fsmState_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _fsmState_ = node;
    }

    public PFsmInputAlpha getFsmInputAlpha()
    {
        return _fsmInputAlpha_;
    }

    public void setFsmInputAlpha(PFsmInputAlpha node)
    {
        if(_fsmInputAlpha_ != null)
        {
            _fsmInputAlpha_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _fsmInputAlpha_ = node;
    }

    public PFsmStart getFsmStart()
    {
        return _fsmStart_;
    }

    public void setFsmStart(PFsmStart node)
    {
        if(_fsmStart_ != null)
        {
            _fsmStart_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _fsmStart_ = node;
    }

    public PFsmForbidden getFsmForbidden()
    {
        return _fsmForbidden_;
    }

    public void setFsmForbidden(PFsmForbidden node)
    {
        if(_fsmForbidden_ != null)
        {
            _fsmForbidden_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _fsmForbidden_ = node;
    }

    public PFsmTransition getFsmTransition()
    {
        return _fsmTransition_;
    }

    public void setFsmTransition(PFsmTransition node)
    {
        if(_fsmTransition_ != null)
        {
            _fsmTransition_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _fsmTransition_ = node;
    }

    public PFsmTrigger getFsmTrigger()
    {
        return _fsmTrigger_;
    }

    public void setFsmTrigger(PFsmTrigger node)
    {
        if(_fsmTrigger_ != null)
        {
            _fsmTrigger_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _fsmTrigger_ = node;
    }

    public PFsmPriority getFsmPriority()
    {
        return _fsmPriority_;
    }

    public void setFsmPriority(PFsmPriority node)
    {
        if(_fsmPriority_ != null)
        {
            _fsmPriority_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _fsmPriority_ = node;
    }

    public PFsmContextTypes getFsmContextTypes()
    {
        return _fsmContextTypes_;
    }

    public void setFsmContextTypes(PFsmContextTypes node)
    {
        if(_fsmContextTypes_ != null)
        {
            _fsmContextTypes_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _fsmContextTypes_ = node;
    }

    public PFsmContextVariables getFsmContextVariables()
    {
        return _fsmContextVariables_;
    }

    public void setFsmContextVariables(PFsmContextVariables node)
    {
        if(_fsmContextVariables_ != null)
        {
            _fsmContextVariables_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _fsmContextVariables_ = node;
    }

    public PFsmPredicateAbbr getFsmPredicateAbbr()
    {
        return _fsmPredicateAbbr_;
    }

    public void setFsmPredicateAbbr(PFsmPredicateAbbr node)
    {
        if(_fsmPredicateAbbr_ != null)
        {
            _fsmPredicateAbbr_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _fsmPredicateAbbr_ = node;
    }

    public PFsmConstraintPair getFsmConstraintPair()
    {
        return _fsmConstraintPair_;
    }

    public void setFsmConstraintPair(PFsmConstraintPair node)
    {
        if(_fsmConstraintPair_ != null)
        {
            _fsmConstraintPair_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _fsmConstraintPair_ = node;
    }

    public String toString()
    {
        return ""
            + toString(_fsmName_)
            + toString(_fsmState_)
            + toString(_fsmInputAlpha_)
            + toString(_fsmStart_)
            + toString(_fsmForbidden_)
            + toString(_fsmTransition_)
            + toString(_fsmTrigger_)
            + toString(_fsmPriority_)
            + toString(_fsmContextTypes_)
            + toString(_fsmContextVariables_)
            + toString(_fsmPredicateAbbr_)
            + toString(_fsmConstraintPair_);
    }

    void removeChild(Node child)
    {
        if(_fsmName_ == child)
        {
            _fsmName_ = null;
            return;
        }

        if(_fsmState_ == child)
        {
            _fsmState_ = null;
            return;
        }

        if(_fsmInputAlpha_ == child)
        {
            _fsmInputAlpha_ = null;
            return;
        }

        if(_fsmStart_ == child)
        {
            _fsmStart_ = null;
            return;
        }

        if(_fsmForbidden_ == child)
        {
            _fsmForbidden_ = null;
            return;
        }

        if(_fsmTransition_ == child)
        {
            _fsmTransition_ = null;
            return;
        }

        if(_fsmTrigger_ == child)
        {
            _fsmTrigger_ = null;
            return;
        }

        if(_fsmPriority_ == child)
        {
            _fsmPriority_ = null;
            return;
        }

        if(_fsmContextTypes_ == child)
        {
            _fsmContextTypes_ = null;
            return;
        }

        if(_fsmContextVariables_ == child)
        {
            _fsmContextVariables_ = null;
            return;
        }

        if(_fsmPredicateAbbr_ == child)
        {
            _fsmPredicateAbbr_ = null;
            return;
        }

        if(_fsmConstraintPair_ == child)
        {
            _fsmConstraintPair_ = null;
            return;
        }

    }

    void replaceChild(Node oldChild, Node newChild)
    {
        if(_fsmName_ == oldChild)
        {
            setFsmName((PFsmName) newChild);
            return;
        }

        if(_fsmState_ == oldChild)
        {
            setFsmState((PFsmState) newChild);
            return;
        }

        if(_fsmInputAlpha_ == oldChild)
        {
            setFsmInputAlpha((PFsmInputAlpha) newChild);
            return;
        }

        if(_fsmStart_ == oldChild)
        {
            setFsmStart((PFsmStart) newChild);
            return;
        }

        if(_fsmForbidden_ == oldChild)
        {
            setFsmForbidden((PFsmForbidden) newChild);
            return;
        }

        if(_fsmTransition_ == oldChild)
        {
            setFsmTransition((PFsmTransition) newChild);
            return;
        }

        if(_fsmTrigger_ == oldChild)
        {
            setFsmTrigger((PFsmTrigger) newChild);
            return;
        }

        if(_fsmPriority_ == oldChild)
        {
            setFsmPriority((PFsmPriority) newChild);
            return;
        }

        if(_fsmContextTypes_ == oldChild)
        {
            setFsmContextTypes((PFsmContextTypes) newChild);
            return;
        }

        if(_fsmContextVariables_ == oldChild)
        {
            setFsmContextVariables((PFsmContextVariables) newChild);
            return;
        }

        if(_fsmPredicateAbbr_ == oldChild)
        {
            setFsmPredicateAbbr((PFsmPredicateAbbr) newChild);
            return;
        }

        if(_fsmConstraintPair_ == oldChild)
        {
            setFsmConstraintPair((PFsmConstraintPair) newChild);
            return;
        }

    }
}
