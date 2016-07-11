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
		qt.add("(2,2)", new Point(2,2));
		qt.add("(2,5)", new Point(2,5));

		qt.add("(2,4)", new Point(2,4));

		qt.add("(2,3)", new Point(2,3));
		qt.add("(8,3)", new Point(8,3));
		
		qt.printQuadTree();
		System.out.println(qt.queryRange(new AABB(new Point(5,5), 5)));
	}

}
