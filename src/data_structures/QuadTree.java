package data_structures;

import java.util.ArrayList;

public class QuadTree {

	public QuadTree nw;
	public QuadTree ne;
	public QuadTree se;
	public QuadTree sw;
	
	public AABB b_box;
	
	private ArrayList<Object> objects;
	private ArrayList<Point> points;
	private int capacity;
	private static final int defaultCapacity = 4;
	
	public QuadTree(AABB aabb){
		this(aabb, defaultCapacity);
	}
	
	public QuadTree(AABB aabb, int capacity){
		this.capacity = capacity;
		b_box = aabb;
		objects = new ArrayList<Object>(capacity);
		points = new ArrayList<Point>(capacity);
	}
	
	public boolean add(Object obj, Point p){
		if(!b_box.containsPoint(p))
			return false;
		if(objects.size() < capacity && nw == null){
			objects.add(obj);
			points.add(p);
			return true;
		}
		if(nw == null)
			subdivide();
		
		if(nw.add(obj, p)) return true;
		if(ne.add(obj, p)) return true;
		if(se.add(obj, p)) return true;
		if(sw.add(obj, p)) return true;
		
		return false;
	}
	
	public ArrayList<Object> queryRange(AABB range){
		ArrayList<Object> dataInRange = new ArrayList<Object>();
		
		if(!b_box.intersectsAABB(range))
			return dataInRange;
		
		// Check objects at this quad level
	    for (int i = 0; i < objects.size(); i++){
	    	Point p = points.get(i);
	    	if (range.containsPoint(p))
	    		dataInRange.add(objects.get(i));
	    }

	    // Terminate here, if there are no children
	    if (nw == null)
	      return dataInRange;

	    // Otherwise, add the points from the children
	    dataInRange.addAll(nw.queryRange(range));
	    dataInRange.addAll(ne.queryRange(range));
	    dataInRange.addAll(se.queryRange(range));
	    dataInRange.addAll(sw.queryRange(range));


	    return dataInRange;
	}
	
	private void subdivide(){
		float halfDimension = b_box.halfDimension/2;
		nw = new QuadTree(new AABB(new Point(b_box.center.x - halfDimension, b_box.center.y + halfDimension), halfDimension), capacity);
		ne = new QuadTree(new AABB(new Point(b_box.center.x + halfDimension, b_box.center.y + halfDimension), halfDimension), capacity);
		se = new QuadTree(new AABB(new Point(b_box.center.x + halfDimension, b_box.center.y - halfDimension), halfDimension), capacity);
		sw = new QuadTree(new AABB(new Point(b_box.center.x - halfDimension, b_box.center.y - halfDimension), halfDimension), capacity);
		pushObjectsDown();
	}
	
	private void pushObjectsDown(){
		while(!objects.isEmpty()){
			Object obj = objects.remove(0);
			Point p = points.remove(0);
			if(nw.add(obj, p)) continue; 
			if(ne.add(obj, p)) continue;
			if(sw.add(obj, p)) continue;
			if(se.add(obj, p)) continue;
		}
	}
	
	public void printQuadTree(){
		printNode("");
	}
	
	public void printNode(String tab){
		System.out.println(tab + objects);
		
		if(nw != null){
			nw.printNode(" " + tab);
		}

		if(ne != null){
			ne.printNode(" " + tab);
		}

		if(sw != null){
			sw.printNode(" " + tab);
		}

		if(se != null){
			se.printNode(" " + tab);
		}

	}
}
