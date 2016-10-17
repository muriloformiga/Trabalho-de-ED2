package br.ufs.ed2.sequencial;

import java.io.FileNotFoundException;

public class Main {

	public static void main(String[] args) throws FileNotFoundException {
		
		Aluno murilo = new Aluno(2014200295, "Murilo Formiga", "Rua Horácio Souza Lima, 390", (short) 20, "046.198.085-35");
		Aluno antonio = new Aluno(2014200290, "Antonio Gilberto", "Rua das Flores, 34", (short) 23, "088.233.434-77");
		Aluno jones = new Aluno(2014200299, "Jones Jefferson", "Praça da Bandeira, 20", (short) 21, "123.443.567-98");
		ManipuladorSequencial ms = new ManipuladorSequencial("C:\\Users\\Murilo\\Desktop\\Arquivos_ED2\\alunos2.txt");
		
		ms.putReg(murilo);
		//ms.getReg(2014200295);
		ms.putReg(antonio);
		ms.putReg(jones);
		//System.out.println(ms.getReg(2014200295));
	}

}
