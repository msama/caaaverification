package uk.ac.ucl.cs.InputMatrix;

public class Transition {
    private String _src;
    private String _input;
    private String _dest;
    
    public Transition(){}
    public Transition(String _src, String _input, String _dest){
    	this._src = _src;
    	this._input = _input;
    	this._dest = _dest;
    }
    public String getSrc(){
    	return _src;
    }
    public String getInput(){
    	return _input;
    }
    public String getDest(){
    	return _dest;
    }
}
