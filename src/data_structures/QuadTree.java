package data_structures;

import java.util.ArrayList;

public class QuadTree {

	public QuadTree nw;
	public QuadTree ne;
	public QuadTree se;
	public QuadTree sw;
	
	public AABB b_box;
	
	public ArrayList<Object> objects;
	public ArrayList<Point> points;
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
	
	/*
	public void rotateLeft(){
		QuadTree temp1 = nw;
		QuadTree temp2 = sw;
		nw = ne;
		sw = se;
		ne = nw;
		sw = se;
	}
	*/
	
	public ArrayList<Object> getAllEast(){
		ArrayList<Object> dataInRange = new ArrayList<Object>();
		dataInRange.addAll(ne.getAllChildren());
		dataInRange.addAll(se.getAllChildren());
		return dataInRange;
	}
	
	public ArrayList<Object> getAllWest(){
		ArrayList<Object> dataInRange = new ArrayList<Object>();
		dataInRange.addAll(nw.getAllChildren());
		dataInRange.addAll(sw.getAllChildren());
		return dataInRange;
	}
	
	public ArrayList<Object> getAllNorth(){
		ArrayList<Object> dataInRange = new ArrayList<Object>();
		dataInRange.addAll(ne.getAllChildren());
		dataInRange.addAll(nw.getAllChildren());
		return dataInRange;
	}
	
	public ArrayList<Object> getAllSouth(){
		ArrayList<Object> dataInRange = new ArrayList<Object>();
		dataInRange.addAll(se.getAllChildren());
		dataInRange.addAll(sw.getAllChildren());
		return dataInRange;
	}
	
	public ArrayList<Object> getAllChildren(){
		ArrayList<Object> dataInRange = new ArrayList<Object>();
		dataInRange.addAll(queryRange(b_box));
		return dataInRange;
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
	
	public ArrayList<Object> queryRangeCircular(AABB range){
		//Create Array of Ranges to Check
		ArrayList<AABB> ranges = new ArrayList<AABB>();
		//Add Initial Range to Check
		range.center = pointToCircularPoint(range.center);
		
		ranges.add(range);
		//Create generic point data for checking
		float rectLeft = range.center.x - range.halfDimension;
		float rectRight = range.center.x + range.halfDimension;
		float rectTop = range.center.y + range.halfDimension;
		float rectBottom = range.center.y - range.halfDimension;
		float qtLeft = b_box.center.x - b_box.halfDimension;
		float qtRight = b_box.center.x + b_box.halfDimension;
		float qtTop = b_box.center.y + b_box.halfDimension;
		float qtBottom = b_box.center.y - b_box.halfDimension;
		//Perform checks and create AABBs to add to range
		boolean top = false, bottom = false, left = false, right = false;
		float rangeHalfDimension = range.halfDimension;
		if(rectLeft < qtLeft){
			right = true;
			AABB rightAABB = new AABB(new Point(range.center.x - qtLeft + qtRight,range.center.y), rangeHalfDimension);
			ranges.add(rightAABB);
		}else if(rectRight > qtRight){
			left = true;
			AABB leftAABB = new AABB(new Point(qtLeft - (qtRight - range.center.x),range.center.y), rangeHalfDimension);
			ranges.add(leftAABB);
		}
		if(rectTop > qtTop){
			bottom = true;
			AABB bottomAABB = new AABB(new Point(range.center.x,qtBottom - range.center.y + qtTop), rangeHalfDimension);
			ranges.add(bottomAABB);
		}else if(rectBottom < qtBottom){
			top = true;
			AABB topAABB = new AABB(new Point(range.center.x,qtTop + (range.center.y - qtBottom)), rangeHalfDimension);
			ranges.add(topAABB);
		}
		if(bottom && right){
			AABB bottomRightAABB = new AABB(new Point(range.center.x - qtLeft + qtRight,
					qtBottom - range.center.y + qtTop), rangeHalfDimension);
			ranges.add(bottomRightAABB);
		}else if(top && right){
			AABB topRightAABB = new AABB(new Point(range.center.x - qtLeft + qtRight,
					qtTop + (range.center.y - qtBottom)), rangeHalfDimension);
			ranges.add(topRightAABB);
		}else if(bottom && left){
			AABB bottomLeftAABB = new AABB(new Point(qtLeft - (qtRight - range.center.x),
					qtBottom - range.center.y + qtTop), rangeHalfDimension);
			ranges.add(bottomLeftAABB);
		}else if(top && left){
			AABB topLeftAABB = new AABB(new Point(qtLeft - (qtRight - range.center.x),
					qtTop + (range.center.y - qtBottom)), rangeHalfDimension);
			ranges.add(topLeftAABB);
		}
		
		//Create array to hold data
		ArrayList<Object> dataInRange = new ArrayList<Object>();
		for(int i = 0; i < ranges.size(); i++){
			dataInRange.addAll(queryRange(ranges.get(i)));
		}
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
	
	private Point pointToCircularPoint(Point point){
		if(point.x > b_box.center.x+b_box.halfDimension || point.x < b_box.center.x-b_box.halfDimension){
			point.x = point.x + 2*b_box.halfDimension % 2*b_box.halfDimension;
		}
		if(point.y > b_box.center.y+b_box.halfDimension || point.y < b_box.center.y-b_box.halfDimension){
			point.y = point.y + 2*b_box.halfDimension % 2*b_box.halfDimension;
		}
		return point;
	}
}
