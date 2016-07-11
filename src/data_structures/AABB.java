package data_structures;

/**
 * Axis Aligned Bounding Box
 * @author Nicole
 *
 */
public class AABB {

	public Point center;
	public float halfDimension;
	
	public AABB(Point center, float halfDimension){
		this.center = center;
		this.halfDimension = halfDimension;
	}
	
	public boolean containsPoint(Point point){
		if(point.x <= center.x+halfDimension && point.x >= center.x-halfDimension){
			if(point.y <= center.y+halfDimension && point.y >= center.y-halfDimension){
				return true;
			}
		}
		return false;
	}
	
	public boolean intersectsAABB(AABB quad){
		float x1_1 = center.x - halfDimension;
		float x2_1 = center.x + halfDimension;
		float y1_1 = center.y + halfDimension;
		float y2_1 = center.y - halfDimension;
		
		float x1_2 = center.x - halfDimension;
		float x2_2 = center.x + halfDimension;
		float y1_2 = center.y + halfDimension;
		float y2_2 = center.y - halfDimension;
		
		if (x1_1 < x2_2 && x2_1 > x1_2 && 
				y1_1 < y2_2 && y2_1 > y1_2) {
			return false;
		}
		System.out.println("true");
		return true;
	}
}
