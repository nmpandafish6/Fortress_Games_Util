package data_structure_gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;

import data_structures.Point;
import data_structures.QuadTree;
import util.MathUtil;

public class QuadTreeGUI {

	public static JPanel drawQuadTree(QuadTree tree, int width, int height){
		JPanel qt = new JPanel(){
			float leftMost = tree.b_box.center.x - tree.b_box.halfDimension;
			float rightMost = tree.b_box.center.x + tree.b_box.halfDimension;
			float topMost = tree.b_box.center.y + tree.b_box.halfDimension;
			float bottomMost = tree.b_box.center.y - tree.b_box.halfDimension;
			
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);  
				paintQT(g, tree);
			}
			
			private void paintQT(Graphics g, QuadTree node){
				//Get Generic Points
				float left = mapX(node.b_box.center.x - node.b_box.halfDimension);
				float right = mapX(node.b_box.center.x + node.b_box.halfDimension);
				float top = mapY(node.b_box.center.y + node.b_box.halfDimension);
				float bottom = mapY(node.b_box.center.y - node.b_box.halfDimension);
				float centerX = mapX(node.b_box.center.x);
				float centerY = mapY(node.b_box.center.y);
				//Draw Lines
				g.drawLine((int) left,(int) centerY,(int) right,(int) centerY);
				g.drawLine((int) centerX,(int) top,(int) centerX,(int) bottom);
				//Draw Points
				for(int i = 0; i < node.points.size(); i++){
					Point p = node.points.get(i);
					float pointX = mapX(p.x);
					float pointY = mapY(p.y);
					float radius = 6;
					g.fillOval((int) (pointX-radius/2), (int) (pointY-radius/2), (int) radius, (int) radius);
				}
				//Repeat Recursively
				if(node.nw != null){
					paintQT(g, node.nw);
				}

				if(node.ne != null){
					paintQT(g, node.ne);
				}

				if(node.se != null){
					paintQT(g, node.se);
				}

				if(node.sw != null){
					paintQT(g, node.sw);
				}
			}
			
			private float mapX(float inputValue){
				return (float) MathUtil.map(inputValue, leftMost, rightMost, 0, width);
			}
			
			private float mapY(float inputValue){
				return (float) MathUtil.map(inputValue, topMost, bottomMost, 0, height);
			}
		};
		qt.setPreferredSize(new Dimension(width,height));
		
		return qt;
	}
}
