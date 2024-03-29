/* This file was generated by SableCC (http://www.sablecc.org/). */

package uk.ac.ucl.cs.InputMatrix.myparser.analysis;

import java.util.*;
import uk.ac.ucl.cs.InputMatrix.myparser.node.*;

public class AnalysisAdapter implements Analysis
{
    private Hashtable in;
    private Hashtable out;

    public Object getIn(Node node)
    {
        if(in == null)
        {
            return null;
        }

        return in.get(node);
    }

    public void setIn(Node node, Object in)
    {
        if(this.in == null)
        {
            this.in = new Hashtable(1);
        }

        if(in != null)
        {
            this.in.put(node, in);
        }
        else
        {
            this.in.remove(node);
        }
    }

    public Object getOut(Node node)
    {
        if(out == null)
        {
            return null;
        }

        return out.get(node);
    }

    public void setOut(Node node, Object out)
    {
        if(this.out == null)
        {
            this.out = new Hashtable(1);
        }

        if(out != null)
        {
            this.out.put(node, out);
        }
        else
        {
            this.out.remove(node);
        }
    }
    public void caseStart(Start node)
    {
        defaultCase(node);
    }

    public void caseAFsmSpecs(AFsmSpecs node)
    {
        defaultCase(node);
    }

    public void caseAFsmSpec(AFsmSpec node)
    {
        defaultCase(node);
    }

    public void caseAFsmName(AFsmName node)
    {
        defaultCase(node);
    }

    public void caseAFsmState(AFsmState node)
    {
        defaultCase(node);
    }

    public void caseASingleStateList(ASingleStateList node)
    {
        defaultCase(node);
    }

    public void caseAScopeStateList(AScopeStateList node)
    {
        defaultCase(node);
    }

    public void caseASequence1StateList(ASequence1StateList node)
    {
        defaultCase(node);
    }

    public void caseASequence2StateList(ASequence2StateList node)
    {
        defaultCase(node);
    }

    public void caseAStateAlpha(AStateAlpha node)
    {
        defaultCase(node);
    }

    public void caseAStateScope(AStateScope node)
    {
        defaultCase(node);
    }

    public void caseAFsmInputAlpha(AFsmInputAlpha node)
    {
        defaultCase(node);
    }

    public void caseASingleInputAlphaList(ASingleInputAlphaList node)
    {
        defaultCase(node);
    }

    public void caseAScopeInputAlphaList(AScopeInputAlphaList node)
    {
        defaultCase(node);
    }

    public void caseASequence1InputAlphaList(ASequence1InputAlphaList node)
    {
        defaultCase(node);
    }

    public void caseASequence2InputAlphaList(ASequence2InputAlphaList node)
    {
        defaultCase(node);
    }

    public void caseAMyInputAlpha(AMyInputAlpha node)
    {
        defaultCase(node);
    }

    public void caseAInputScope(AInputScope node)
    {
        defaultCase(node);
    }

    public void caseAFsmStart(AFsmStart node)
    {
        defaultCase(node);
    }

    public void caseAStartAlpha(AStartAlpha node)
    {
        defaultCase(node);
    }

    public void caseAFsmForbidden(AFsmForbidden node)
    {
        defaultCase(node);
    }

    public void caseAForbiddenStateList(AForbiddenStateList node)
    {
        defaultCase(node);
    }

    public void caseAEmptyForbiddenStateList(AEmptyForbiddenStateList node)
    {
        defaultCase(node);
    }

    public void caseAFsmTransition(AFsmTransition node)
    {
        defaultCase(node);
    }

    public void caseASingleTransitionList(ASingleTransitionList node)
    {
        defaultCase(node);
    }

    public void caseASequenceTransitionList(ASequenceTransitionList node)
    {
        defaultCase(node);
    }

    public void caseAFsmOneTransition(AFsmOneTransition node)
    {
        defaultCase(node);
    }

    public void caseAMyStateAlpha1(AMyStateAlpha1 node)
    {
        defaultCase(node);
    }

    public void caseAMyTransitionInputAlpha(AMyTransitionInputAlpha node)
    {
        defaultCase(node);
    }

    public void caseAMyStateAlpha2(AMyStateAlpha2 node)
    {
        defaultCase(node);
    }

    public void caseAFsmTrigger(AFsmTrigger node)
    {
        defaultCase(node);
    }

    public void caseASingleTriggerList(ASingleTriggerList node)
    {
        defaultCase(node);
    }

    public void caseASequenceTriggerList(ASequenceTriggerList node)
    {
        defaultCase(node);
    }

    public void caseAStatetriggerMyTrigger(AStatetriggerMyTrigger node)
    {
        defaultCase(node);
    }

    public void caseAMyTriggerStateAlpha(AMyTriggerStateAlpha node)
    {
        defaultCase(node);
    }

    public void caseASingleTriggers(ASingleTriggers node)
    {
        defaultCase(node);
    }

    public void caseASequenceTriggers(ASequenceTriggers node)
    {
        defaultCase(node);
    }

    public void caseAAtrigger(AAtrigger node)
    {
        defaultCase(node);
    }

    public void caseAFsmPriority(AFsmPriority node)
    {
        defaultCase(node);
    }

    public void caseAEmptyFsmPriority(AEmptyFsmPriority node)
    {
        defaultCase(node);
    }

    public void caseASinglePriorityList(ASinglePriorityList node)
    {
        defaultCase(node);
    }

    public void caseASequencePriorityList(ASequencePriorityList node)
    {
        defaultCase(node);
    }

    public void caseATriggerpriorityMyPriority(ATriggerpriorityMyPriority node)
    {
        defaultCase(node);
    }

    public void caseAMyTriggerAlpha(AMyTriggerAlpha node)
    {
        defaultCase(node);
    }

    public void caseAIntegerPrioritySetting(AIntegerPrioritySetting node)
    {
        defaultCase(node);
    }

    public void caseAFsmContextTypes(AFsmContextTypes node)
    {
        defaultCase(node);
    }

    public void caseASingleTypeList(ASingleTypeList node)
    {
        defaultCase(node);
    }

    public void caseASequenceTypeList(ASequenceTypeList node)
    {
        defaultCase(node);
    }

    public void caseAMyType(AMyType node)
    {
        defaultCase(node);
    }

    public void caseATypeAbbr(ATypeAbbr node)
    {
        defaultCase(node);
    }

    public void caseATypeName(ATypeName node)
    {
        defaultCase(node);
    }

    public void caseAIntegerRefreshRate(AIntegerRefreshRate node)
    {
        defaultCase(node);
    }

    public void caseAFsmContextVariables(AFsmContextVariables node)
    {
        defaultCase(node);
    }

    public void caseASingleVariableList(ASingleVariableList node)
    {
        defaultCase(node);
    }

    public void caseASequenceVariableList(ASequenceVariableList node)
    {
        defaultCase(node);
    }

    public void caseAMyVariable(AMyVariable node)
    {
        defaultCase(node);
    }

    public void caseAVariableName(AVariableName node)
    {
        defaultCase(node);
    }

    public void caseAVarTypeName(AVarTypeName node)
    {
        defaultCase(node);
    }

    public void caseAFsmPredicateAbbr(AFsmPredicateAbbr node)
    {
        defaultCase(node);
    }

    public void caseASinglePredicateList(ASinglePredicateList node)
    {
        defaultCase(node);
    }

    public void caseASequencePredicateList(ASequencePredicateList node)
    {
        defaultCase(node);
    }

    public void caseAMyPredicate(AMyPredicate node)
    {
        defaultCase(node);
    }

    public void caseAPredNamePredicateName(APredNamePredicateName node)
    {
        defaultCase(node);
    }

    public void caseAPredicateLogic(APredicateLogic node)
    {
        defaultCase(node);
    }

    public void caseALogicOrExp(ALogicOrExp node)
    {
        defaultCase(node);
    }

    public void caseAEmptyLogicOrExp(AEmptyLogicOrExp node)
    {
        defaultCase(node);
    }

    public void caseALogicAndExp(ALogicAndExp node)
    {
        defaultCase(node);
    }

    public void caseAEmptyLogicAndExp(AEmptyLogicAndExp node)
    {
        defaultCase(node);
    }

    public void caseANotunaryUnaryExp(ANotunaryUnaryExp node)
    {
        defaultCase(node);
    }

    public void caseAPrimexpUnaryExp(APrimexpUnaryExp node)
    {
        defaultCase(node);
    }

    public void caseAExpPrimaryExp(AExpPrimaryExp node)
    {
        defaultCase(node);
    }

    public void caseAParenexpPrimaryExp(AParenexpPrimaryExp node)
    {
        defaultCase(node);
    }

    public void caseAParenExp(AParenExp node)
    {
        defaultCase(node);
    }

    public void caseAFsmConstraintPair(AFsmConstraintPair node)
    {
        defaultCase(node);
    }

    public void caseASingleConstraintPairList(ASingleConstraintPairList node)
    {
        defaultCase(node);
    }

    public void caseASequenceConstraintPairList(ASequenceConstraintPairList node)
    {
        defaultCase(node);
    }

    public void caseAMyConstraintPair(AMyConstraintPair node)
    {
        defaultCase(node);
    }

    public void caseAConditionPredicate(AConditionPredicate node)
    {
        defaultCase(node);
    }

    public void caseAEffectPredicate(AEffectPredicate node)
    {
        defaultCase(node);
    }

    public void caseAFsmVariableAbbr(AFsmVariableAbbr node)
    {
        defaultCase(node);
    }

    public void caseASingleAbbrVariableList(ASingleAbbrVariableList node)
    {
        defaultCase(node);
    }

    public void caseASequenceAbbrVariableList(ASequenceAbbrVariableList node)
    {
        defaultCase(node);
    }

    public void caseAOneVariablePair(AOneVariablePair node)
    {
        defaultCase(node);
    }

    public void caseAAbbrVariableName(AAbbrVariableName node)
    {
        defaultCase(node);
    }

    public void caseABtBinaryLogicExp(ABtBinaryLogicExp node)
    {
        defaultCase(node);
    }

    public void caseADistanceBinaryLogicExp(ADistanceBinaryLogicExp node)
    {
        defaultCase(node);
    }

    public void caseACommonBinaryLogicExp(ACommonBinaryLogicExp node)
    {
        defaultCase(node);
    }

    public void caseALogicEqEqualityExp(ALogicEqEqualityExp node)
    {
        defaultCase(node);
    }

    public void caseALogicNeqEqualityExp(ALogicNeqEqualityExp node)
    {
        defaultCase(node);
    }

    public void caseAEmptyEqualityExp(AEmptyEqualityExp node)
    {
        defaultCase(node);
    }

    public void caseALogicGtRelationExp(ALogicGtRelationExp node)
    {
        defaultCase(node);
    }

    public void caseALogicLtRelationExp(ALogicLtRelationExp node)
    {
        defaultCase(node);
    }

    public void caseALogicLeRelationExp(ALogicLeRelationExp node)
    {
        defaultCase(node);
    }

    public void caseALogicGeRelationExp(ALogicGeRelationExp node)
    {
        defaultCase(node);
    }

    public void caseAEmptyRelationExp(AEmptyRelationExp node)
    {
        defaultCase(node);
    }

    public void caseAMyUnaryExp(AMyUnaryExp node)
    {
        defaultCase(node);
    }

    public void caseABtClients(ABtClients node)
    {
        defaultCase(node);
    }

    public void caseABtExpBtValueList(ABtExpBtValueList node)
    {
        defaultCase(node);
    }

    public void caseABtSetName(ABtSetName node)
    {
        defaultCase(node);
    }

    public void caseADistanceFunc(ADistanceFunc node)
    {
        defaultCase(node);
    }

    public void caseADistanceValue(ADistanceValue node)
    {
        defaultCase(node);
    }

    public void caseALocationName(ALocationName node)
    {
        defaultCase(node);
    }

    public void caseAGtEqualityLogic(AGtEqualityLogic node)
    {
        defaultCase(node);
    }

    public void caseALtEqualityLogic(ALtEqualityLogic node)
    {
        defaultCase(node);
    }

    public void caseAEqEqualityLogic(AEqEqualityLogic node)
    {
        defaultCase(node);
    }

    public void caseALeEqualityLogic(ALeEqualityLogic node)
    {
        defaultCase(node);
    }

    public void caseANeqEqualityLogic(ANeqEqualityLogic node)
    {
        defaultCase(node);
    }

    public void caseAGeEqualityLogic(AGeEqualityLogic node)
    {
        defaultCase(node);
    }

    public void caseAIdentifierFactor(AIdentifierFactor node)
    {
        defaultCase(node);
    }

    public void caseAIntegerFactor(AIntegerFactor node)
    {
        defaultCase(node);
    }

    public void caseTHeadPos(THeadPos node)
    {
        defaultCase(node);
    }

    public void caseTBlank(TBlank node)
    {
        defaultCase(node);
    }

    public void caseTSpace(TSpace node)
    {
        defaultCase(node);
    }

    public void caseTCrlf(TCrlf node)
    {
        defaultCase(node);
    }

    public void caseTLBrace(TLBrace node)
    {
        defaultCase(node);
    }

    public void caseTRBrace(TRBrace node)
    {
        defaultCase(node);
    }

    public void caseTArrow(TArrow node)
    {
        defaultCase(node);
    }

    public void caseTLPar(TLPar node)
    {
        defaultCase(node);
    }

    public void caseTRPar(TRPar node)
    {
        defaultCase(node);
    }

    public void caseTString(TString node)
    {
        defaultCase(node);
    }

    public void caseTComma(TComma node)
    {
        defaultCase(node);
    }

    public void caseTHyphen(THyphen node)
    {
        defaultCase(node);
    }

    public void caseTEqual(TEqual node)
    {
        defaultCase(node);
    }

    public void caseTName(TName node)
    {
        defaultCase(node);
    }

    public void caseTState(TState node)
    {
        defaultCase(node);
    }

    public void caseTInputAlpha(TInputAlpha node)
    {
        defaultCase(node);
    }

    public void caseTStart(TStart node)
    {
        defaultCase(node);
    }

    public void caseTForbidden(TForbidden node)
    {
        defaultCase(node);
    }

    public void caseTTransitionFunc(TTransitionFunc node)
    {
        defaultCase(node);
    }

    public void caseTTrigger(TTrigger node)
    {
        defaultCase(node);
    }

    public void caseTPriority(TPriority node)
    {
        defaultCase(node);
    }

    public void caseTContextTypes(TContextTypes node)
    {
        defaultCase(node);
    }

    public void caseTContextVariables(TContextVariables node)
    {
        defaultCase(node);
    }

    public void caseTPredicateAbbr(TPredicateAbbr node)
    {
        defaultCase(node);
    }

    public void caseTPredicatesForState(TPredicatesForState node)
    {
        defaultCase(node);
    }

    public void caseTConstraintPair(TConstraintPair node)
    {
        defaultCase(node);
    }

    public void caseTVariableAbbr(TVariableAbbr node)
    {
        defaultCase(node);
    }

    public void caseTId(TId node)
    {
        defaultCase(node);
    }

    public void caseTInt(TInt node)
    {
        defaultCase(node);
    }

    public void caseTScopeChar(TScopeChar node)
    {
        defaultCase(node);
    }

    public void caseTMyChar(TMyChar node)
    {
        defaultCase(node);
    }

    public void caseTNumber(TNumber node)
    {
        defaultCase(node);
    }

    public void caseTPrioritySettingValue(TPrioritySettingValue node)
    {
        defaultCase(node);
    }

    public void caseTLOr(TLOr node)
    {
        defaultCase(node);
    }

    public void caseTLAnd(TLAnd node)
    {
        defaultCase(node);
    }

    public void caseTLNot(TLNot node)
    {
        defaultCase(node);
    }

    public void caseTLIn(TLIn node)
    {
        defaultCase(node);
    }

    public void caseTLGt(TLGt node)
    {
        defaultCase(node);
    }

    public void caseTLLt(TLLt node)
    {
        defaultCase(node);
    }

    public void caseTLEq(TLEq node)
    {
        defaultCase(node);
    }

    public void caseTLLe(TLLe node)
    {
        defaultCase(node);
    }

    public void caseTLNeq(TLNeq node)
    {
        defaultCase(node);
    }

    public void caseTLGe(TLGe node)
    {
        defaultCase(node);
    }

    public void caseTDist(TDist node)
    {
        defaultCase(node);
    }

    public void caseEOF(EOF node)
    {
        defaultCase(node);
    }

    public void defaultCase(Node node)
    {
    }
}
