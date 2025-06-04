package YutYutGameFX;

public class Rule {

    private int distance = 0;              // 현재 이동 거리
    private int currentPlayerIndex = 0;    // 현재 턴 플레이어 인덱스
    private final int playerCount;         // 총 플레이어 수

    private int remainingRolls = 1;        // 남은 윷 던지기 횟수
    private boolean yutIsEmpty = false;    // 윷 결과 모두 소진 여부
    private boolean pieceMoved = false;    // 이번 턴에 실제 말 이동 여부
    
    private Yut yut;

    public Rule(int playerCount, Yut yut) {
        this.playerCount = playerCount;
        this.yut = yut;
        resetTurnState();
    }

    // 윷 결과 관련
    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getDistance() {
        return distance;
    }

    // 턴과 플레이어 관련
    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    // 상태 업데이트
    public void markYutUsedUp() {
        this.yutIsEmpty = true;
    }

    public void markPieceMoved(boolean moved) {
        this.pieceMoved = moved;
    }

    public void addRollChance() {
        remainingRolls++;
    }

    public void useRollChance() {
        if (remainingRolls > 0) {
            remainingRolls--;
        }
    }

    public int getRemainingRolls() {
        return remainingRolls;
    }

    public boolean isYutUsedUp() {
        return yutIsEmpty;
    }
    
    public void test() {
    	yut.test();
    }

    public boolean hasPieceMoved() {
        return pieceMoved;
    }

    // 턴 변경 판단 및 처리
    public void changePlayerIfTurnDone() {
        if (remainingRolls == 0 && yut.isAllUsed() && pieceMoved) {
            currentPlayerIndex = (currentPlayerIndex + 1) % playerCount;
            resetTurnState();
        }
    }


    public void forceNextTurn() {
        currentPlayerIndex = (currentPlayerIndex + 1) % playerCount;
        resetTurnState();
    }

    public void resetTurnState() {
        yutIsEmpty = false;
        pieceMoved = false;
        distance = 0;
        remainingRolls = 1;
    }
    
    
}
//바뀜