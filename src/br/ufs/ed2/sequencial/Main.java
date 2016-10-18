package br.ufs.ed2.sequencial;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import br.ufs.ed2.brent.OrganizadorBrent;

public class Main {

	public static void main(String[] args) throws IOException {

		//Aluno murilo = new Aluno(2014200295, "Murilo Formiga", "Rua Horácio Souza Lima, 390", (short) 20, "046.198.085-35");
		//Aluno antonio = new Aluno(2014200290, "Antonio Gilberto", "Rua das Flores, 34", (short) 23, "088.233.434-77");
		//Aluno jones = new Aluno(2014200299, "Jones Jefferson", "Praça da Bandeira, 20", (short) 21, "123.443.567-98");
		//ManipuladorSequencial ms = new ManipuladorSequencial("C:\\Users\\Murilo\\Desktop\\Arquivos_ED2\\alunos2.txt");

		File fOrigem = new File("enem_aleat.db");
		RandomAccessFile fileOrigem = new RandomAccessFile("C:\\Users\\Murilo\\Desktop\\ED-II SI\\enem_aleat.db", "r");
		FileChannel channelOrigem = fileOrigem.getChannel();

		//IFileOrganizer orgBrent = new OrganizadorBrent("C:\\Users\\Murilo\\Desktop\\ED-II SI\\enem_brent.db");

		File fDestino = new File("enem_brent.db"); // referencia o arquivo organizado pelo método implementado
		IFileOrganizer org = new OrganizadorBrent("C:\\Users\\Murilo\\Desktop\\ED-II SI\\enem_brent.db");

		// Ler cada aluno do arquivo de origem e inserir no de destino
		for (int i=0; i < 8478096; i++)  {
			// Ler da origem
			ByteBuffer buff = ByteBuffer.allocate(165);
			channelOrigem.read(buff);
			System.out.println(i + " :: " + buff.getLong(0));
			buff.flip();
			Aluno a = new Aluno(buff);
			// Inserir no destino
			org.putReg(a);
		}
		channelOrigem.close();
	}

}
