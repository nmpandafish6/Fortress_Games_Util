import javax.swing.JFrame;
import javax.swing.JPanel;

import data_structure_gui.QuadTreeGUI;
import data_structures.*;

public class Master {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		AABB b_box = new AABB(new Point(0,0), 10);
		QuadTree qt = new QuadTree(b_box);
		qt.add("(1,1)", new Point(1,1));
		qt.add("(1,-1)", new Point(1,-1));
		qt.add("(-1,1)", new Point(-1,1));
		qt.add("(-1,-1)", new Point(-1,-1));
		
		qt.add("(0,9)", new Point(0,9));
		qt.add("(0,-9)", new Point(0,-9));
		qt.add("(-9,0)", new Point(-9,0));
		qt.add("(9,0)", new Point(9,0));

		qt.add("(9,9)", new Point(9,9));
		qt.add("(9,-9)", new Point(9,-9));
		qt.add("(-9,9)", new Point(-9,9));
		qt.add("(-9,-9)", new Point(-9,-9));
		
		//qt.printQuadTree();
		System.out.println(qt.queryRangeCircular(new AABB(new Point(-10,10), 3)));
		
		JPanel qt_gui = QuadTreeGUI.drawQuadTree(qt, 500, 500);
		
		JFrame frame = new JFrame();
		frame.add(qt_gui);
		frame.pack();
		frame.setVisible(true);
	}

}
