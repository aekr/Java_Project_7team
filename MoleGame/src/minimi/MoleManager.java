/*
 * package minimi;
 * 
 * //두더지를 관리하는 클래스 public class MoleManager { private MoleHole[][] holes = new
 * MoleHole[4][4];
 * 
 * public MoleManager() { // 두더지 홀 초기화 for (int i = 0; i < holes.length; i++) {
 * for (int j = 0; j < holes[i].length; j++) { holes[i][j] = new MoleHole(); } }
 * // 두더지 타이머 시작 startMoleTimer(); }
 * 
 * private void startMoleTimer() { // 각 홀에 대해 타이머 설정 for (int i = 0; i <
 * holes.length; i++) { for (int j = 0; j < holes[i].length; j++) {
 * scheduleMolePop(holes[i][j]); } } }
 * 
 * private void scheduleMolePop(MoleHole hole) { // 0.5초에서 1초 사이의 랜덤 시간 설정 int
 * delay = 500 + new Random().nextInt(500); new Timer().schedule(new TimerTask()
 * {
 * 
 * @Override public void run() { hole.popMole(); // 다음 두더지를 예약
 * scheduleMolePop(hole); } }, delay); } }
 */