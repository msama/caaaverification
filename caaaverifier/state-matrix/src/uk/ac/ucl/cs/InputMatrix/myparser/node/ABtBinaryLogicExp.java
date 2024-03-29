/* This file was generated by SableCC (http://www.sablecc.org/). */

package uk.ac.ucl.cs.InputMatrix.myparser.node;

import java.util.*;
import uk.ac.ucl.cs.InputMatrix.myparser.analysis.*;

public final class ABtBinaryLogicExp extends PBinaryLogicExp
{
    private PBtClients _btClients_;
    private TLIn _lIn_;
    private PBtSetName _btSetName_;

    public ABtBinaryLogicExp()
    {
    }

    public ABtBinaryLogicExp(
        PBtClients _btClients_,
        TLIn _lIn_,
        PBtSetName _btSetName_)
    {
        setBtClients(_btClients_);

        setLIn(_lIn_);

        setBtSetName(_btSetName_);

    }
    public Object clone()
    {
        return new ABtBinaryLogicExp(
            (PBtClients) cloneNode(_btClients_),
            (TLIn) cloneNode(_lIn_),
            (PBtSetName) cloneNode(_btSetName_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseABtBinaryLogicExp(this);
    }

    public PBtClients getBtClients()
    {
        return _btClients_;
    }

    public void setBtClients(PBtClients node)
    {
        if(_btClients_ != null)
        {
            _btClients_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _btClients_ = node;
    }

    public TLIn getLIn()
    {
        return _lIn_;
    }

    public void setLIn(TLIn node)
    {
        if(_lIn_ != null)
        {
            _lIn_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _lIn_ = node;
    }

    public PBtSetName getBtSetName()
    {
        return _btSetName_;
    }

    public void setBtSetName(PBtSetName node)
    {
        if(_btSetName_ != null)
        {
            _btSetName_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _btSetName_ = node;
    }

    public String toString()
    {
        return ""
            + toString(_btClients_)
            + toString(_lIn_)
            + toString(_btSetName_);
    }

    void removeChild(Node child)
    {
        if(_btClients_ == child)
        {
            _btClients_ = null;
            return;
        }

        if(_lIn_ == child)
        {
            _lIn_ = null;
            return;
        }

        if(_btSetName_ == child)
        {
            _btSetName_ = null;
            return;
        }

    }

    void replaceChild(Node oldChild, Node newChild)
    {
        if(_btClients_ == oldChild)
        {
            setBtClients((PBtClients) newChild);
            return;
        }

        if(_lIn_ == oldChild)
        {
            setLIn((TLIn) newChild);
            return;
        }

        if(_btSetName_ == oldChild)
        {
            setBtSetName((PBtSetName) newChild);
            return;
        }

    }
}
