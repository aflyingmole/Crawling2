package user;

import ui.ButtonDemo;

public class Operator {
	Database db = null;
	MainFrame mf = null;
	JoinFrame jf = null;
	ButtonDemo buttonDemo = null;

	public Operator() {
		db = new Database();
		mf = new MainFrame(this);
		jf = new JoinFrame(this);
	}

	public void runButtonDemo() {
		buttonDemo = new ButtonDemo();
		buttonDemo.setVisible(true);
	}

	public static void main(String[] args) {
		Operator opt = new Operator();
	}
}
