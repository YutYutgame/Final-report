package YutYutGame;

import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;

public class Yut {
	
    private String[] outcomes = {"빽도", "도", "개", "걸", "윷", "모"};
    private List<Integer> result = new ArrayList<>();

    public int throwYutRand() {
        int idx = (int) (Math.random() * outcomes.length);
        return idx;
    }

    public String[] getOutcomes() {
        return outcomes;
    }
    
    public int getIdxOutcomes(String selected) {
    	for (int i = 0; i < outcomes.length; i++) {
    	    if (outcomes[i].equals(selected)) return (i==0 ? -1:i);
    	}
    	return 0;
    }
    
    public boolean useResult(int idx) {
        return result.remove(Integer.valueOf(idx));
    }
    
    public void setSelectedResult(int idx) {
    	result.add(idx); 	
    	
    }
    public void test() {
    	System.out.println("[턴 검사] 결과 리스트: yut" + result);
    }
    
    public boolean isAllUsed() {
        return result.isEmpty();
    }
    public List<Integer> getResultList() {
        return result;
    }
}
