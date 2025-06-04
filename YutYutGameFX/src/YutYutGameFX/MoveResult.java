package YutYutGameFX;

import java.util.ArrayList;
import java.util.List;

public class MoveResult {
    private final List<Piece> captured = new ArrayList<>();
    private final List<Piece> escaped = new ArrayList<>();
    private boolean blocked = false;
    private boolean capturedOccurred = false;

    // 잡힌 말 추가
    public void addCaptured(Piece p) {
        captured.add(p);
    }

    // 여러 말이 한 번에 잡힐 경우
    public void addAllCaptured(List<Piece> pieces) {
        captured.addAll(pieces);
    }

    // 탈출한 말 추가
    public void addEscaped(Piece p) {
        escaped.add(p);
    }
    
    public void setCapturedOccurred(boolean capturedOccurred) {
        this.capturedOccurred = capturedOccurred;
    }

    public boolean hasCapturedOccurred() {
        return capturedOccurred;
    }

    // 결과 가져오기
    public List<Piece> getCaptured() {
        return captured;
    }

    public List<Piece> getEscaped() {
        return escaped;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    // 결과가 아무것도 없을 경우 확인용
    public boolean isEmpty() {
        return captured.isEmpty() && escaped.isEmpty() && !blocked;
    }
}
//새로 생김