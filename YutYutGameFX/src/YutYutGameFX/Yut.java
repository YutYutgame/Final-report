package YutYutGameFX;

import java.util.ArrayList;
import java.util.List;

public class Yut {

    private final String[] outcomes = {"빽도", "도", "개", "걸", "윷", "모"};
    private final List<Integer> result = new ArrayList<>();

    public int throwYutRand() {
        return (int) (Math.random() * outcomes.length);
    }

    public String[] getOutcomes() {
        return outcomes;
    }

    public int getIdxOutcomes(String selected) {
        for (int i = 0; i < outcomes.length; i++) {
            if (outcomes[i].equals(selected)) {
                return (i == 0) ? -1 : i;
            }
        }
        return 0; // 기본값으로 도
    }

    public boolean useResult(int idx) {
        return result.remove(Integer.valueOf(idx));
    }

    public void setSelectedResult(int idx) {
        result.add(idx);
    }

    public boolean isAllUsed() {
        return result.isEmpty();
    }

    public List<Integer> getResultList() {
        return result;
    }

    // 디버깅용 출력
    public void test() {
        System.out.println("[턴 검사] 결과 리스트: yut " + result);
    }
}
