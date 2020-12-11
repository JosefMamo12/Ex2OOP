package gameClient;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import Server.Game_Server_Ex2;
import actualClasses.DW_GraphAlgo;
import api.directed_weighted_graph;
import api.dw_graph_algorithms;
import api.game_service;
import api.node_data;

	

public class Ex2 {
	public static String fileCreator (String str) {
		File file = new File("gameGraph.txt");
		try {
			if(!file.exists()) {
			
				file.createNewFile();
			}
			PrintWriter pw = new PrintWriter(file);
			pw.println(str);
			pw.close();
			System.out.println("Done");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return file.getName();
	}
	public static void main(String[] args) throws FileNotFoundException {
		int level_number = 0;
		game_service game = Game_Server_Ex2.getServer(level_number);
		game.startGame();
		game.addAgent(0);
		game.addAgent(2);
		game.getAgents();
	dw_graph_algorithms ga = new DW_GraphAlgo();
	ga.load(fileCreator(game.getGraph()));
	
	while(game.isRunning()) {

	}



}
}

