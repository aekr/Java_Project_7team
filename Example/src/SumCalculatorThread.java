public class SumCalculatorThread extends Thread {
	private int start;
	private int end;
	private int result;
	public SumCalculatorThread(int start, int end) {
		this.start = start;
		this.end = end;
	}
@Override
	public void run() {
	for (int i = start; i <= end; i++) {
		result += i;
	}
	System.out.println(
			"Thread " + Thread.currentThread().getName() + ": Sum from " + start + " to " + end + " is: " + result);
	}
	public int getResult() {
		return result;
	}
	public static void main(String[] args) {
		SumCalculatorThread thread1 = new SumCalculatorThread(1, 50);
		SumCalculatorThread thread2 = new SumCalculatorThread(51, 100);
		thread1.start();
		thread2.start();
		try {
			thread1.join(); // Wait for thread1 to finish
			thread2.join(); // Wait for thread2 to finish
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		int totalSum = thread1.getResult() + thread2.getResult();
		System.out.println("Total sum from 1 to 100 is: " + totalSum);
	}
}