
public class MoleHole {
    private boolean hasMole;

    // 두더지가 있는지 확인하는 메소드
    public synchronized boolean checkAndHit() {
        if (hasMole) {
            hasMole = false;
            // 여기에 두더지를 맞췄을 때의 로직을 추가
            return true;
        }
        return false;
    }
    public synchronized void popMole() {
        hasMole = true;
        // 여기에 두더지가 나타났을 때의 로직을 추가
    }
}