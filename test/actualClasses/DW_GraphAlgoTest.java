package actualClasses;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import api.directed_weighted_graph;
import api.dw_graph_algorithms;

class DW_GraphAlgoTest {

	@Test
	void savetest() {
		directed_weighted_graph g = new DW_GraphDS();
		int v = 10;
		for (int i = 0; i < v ; i++) {
			g.addNode(new NodeData(i));
		}
	g.connect(0, 1, 2.5);
	g.connect(1, 2, 3.8);
	g.connect(1,5,3.2);
	dw_graph_algorithms g1 = new DW_GraphAlgo();
	g1.init(g);
	g1.save("graph.jason");
			
	}
	@Test
	void loadtest() {
		directed_weighted_graph g = new DW_GraphDS();
		dw_graph_algorithms g1 = new DW_GraphAlgo();
		g1.load("graph.json");
		
	}

}
