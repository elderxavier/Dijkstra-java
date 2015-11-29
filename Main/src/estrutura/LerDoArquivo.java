package estrutura;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import algoritmos.Dijkstra;


public class LerDoArquivo{
	
	public static List<Vertice> lerGrafo(String nomeArquivo){
	
		Grafo g = new Grafo();
		Vertice v;
		File f = new File(nomeArquivo);
		String vertices[];
		String linha;
		ArrayList<String[]> s1 = new ArrayList<String[]>();
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(f));

			Map<String,Vertice> mapa = new HashMap<String,Vertice>(); 
  			
	
			while ((linha = br.readLine()) != null) {
	
				if (linha.contains(",")) {
					s1.add(linha.split("/"));
					vertices = s1.get(0)[0].split(",");

					//System.out.println("**** GRAFO ****"+g);
					v = (Vertice) mapa.get(vertices[0]);
					//System.out.println("Vertice="+vertices[0]+"-"+v);
					if (v == null) v = new Vertice();
						

					List<Vertice> vizinhosAtual = new ArrayList<Vertice>();
					List<Aresta> arestasAtual = new ArrayList<Aresta>();
					v.setDescricao(vertices[0]);
					mapa.put(vertices[0], v);
	
					if (linha.contains("/")) {
	
						String pesoArestas[] = s1.get(0)[1].split(",");
	
						for (int i = 1; i < vertices.length; i++) {
							Vertice vit;
					//vit = g.encontrarVertice(vertices[i]);
					vit =  mapa.get(vertices[i]);
					if (vit == null) vit = new Vertice();
							vit.setDescricao(vertices[i]);
							vizinhosAtual.add(vit);
							mapa.put(vertices[i], vit);
	
							
	
							Aresta ait = new Aresta(v, vit);
							ait.setPeso(Integer.parseInt(pesoArestas[i - 1]));
							arestasAtual.add(ait);
	
						}
						v.setVizinhos(vizinhosAtual);
						v.setArestas(arestasAtual);
	
					}
	
	
				}
	
				// Vertices finais
				else {

					//v = g.encontrarVertice(linha);
					v = (Vertice) mapa.get(linha);
					//System.out.println("Vertice="+linha+"-"+v);
					if (v == null) v = new Vertice();
					v.setDescricao(linha);
					mapa.put(linha, v);
	
				}
	
				g.adicionarVertice(v);
				//System.out.println("**** GRAFO ****"+g);
				s1.clear();

			}
	
			// catch do BufferedReader
		} catch (FileNotFoundException e) {
			//System.out.println("Nao encontrou o arquivo");
			e.printStackTrace();
		}
		// catch do readLine
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*//System.out.println("Perceba que todos os vizinhos estao no lugar certo:");
		//System.out.println("Imprimindo a lista de vertices:" + g.getVertices());
		//System.out.println("Vizinhos de v1" + g.getVertices().get(0).getVizinhos());
		//System.out.println("Vizinhos de v2" + g.getVertices().get(1).getVizinhos());
		//System.out.println("Vizinhos de v3" + g.getVertices().get(2).getVizinhos());
		//System.out.println("Vizinhos de v4" + g.getVertices().get(3).getVizinhos());
		//System.out.println("Testando os pesos:");
		//System.out.println();
		//System.out.println("Peso da aresta 1: " +g.getVertices().get(0).getArestas().get(0).getPeso());
		//System.out.println("Peso da segunda aresta em 1: " +g.getVertices().get(0).getArestas().get(1).getPeso());
		//System.out.println("Peso da aresta em 2: " +g.getVertices().get(1).getArestas().get(0).getPeso());
		//System.out.println("Peso da aresta 3: " +g.getVertices().get(2).getArestas().get(0).getPeso());*/
		return g.getVertices();
	}
	
	public static void main(String args[]) {
		
		Grafo teste = new Grafo();
		
		//teste.setVertices(lerGrafo(args[0]));
                teste.setVertices(lerGrafo("teste.txt"));
                
		Vertice i1 = new Vertice();
		Vertice i2 = new Vertice();
		//i1 = teste.encontrarVertice(args[1]);
		//i2 = teste.encontrarVertice(args[2]);		
                i1 = teste.encontrarVertice("v1");
		i2 = teste.encontrarVertice("v4");		
		
		List<Vertice> resultado = new ArrayList<Vertice>();
		Dijkstra algoritmo = new Dijkstra();
		resultado = algoritmo.encontrarMenorCaminhoDijkstra(teste, i1, i2);
	
		
			System.out.println("Esse é o menor caminho feito pelo algoritmo:" + resultado);
	}
		
	
	}