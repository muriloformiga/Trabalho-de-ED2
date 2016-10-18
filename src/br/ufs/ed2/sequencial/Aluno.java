package br.ufs.ed2.sequencial;

import java.nio.ByteBuffer;

public class Aluno {

	public static final int TAMANHO_ALUNO = 165;
	public static final int TAMANHO_MATRIC = 8;
	
	private long matric;		// 8  bytes
	private String nome;		// 60 bytes
	private String endereco;	// 80 bytes
	private short idade;		// 2  bytes
	private String cpf;			// 15 bytes
	
	// Construtor da Classe
	public Aluno (long matric, String nome, String endereco, short idade, String cpf) {
		this.matric = matric;
		this.nome = corrigirTamanho(nome, 60);
		this.endereco = corrigirTamanho(endereco, 80);
		this.idade = idade;
		this.cpf = corrigirTamanho(cpf, 15);
	}
	
	public Aluno (ByteBuffer buf) {
		
		this.matric = buf.getLong();
		
		byte[] vNome = new byte[60];
		buf.get(vNome);
		this.nome = new String(vNome);
		
		byte[] vEndereco = new byte[80];
		buf.get(vEndereco);
		this.endereco = new String(vEndereco);
		
		this.idade = buf.getShort();
		
		byte[] vCpf = new byte[15];
		buf.get(vCpf);
		this.cpf = new String(vCpf);
	}
	
	private String corrigirTamanho (String s, int size) {	
		if (s.length() < size)
			while (s.length() < size)
				s = s + " ";
		else
			s = s.substring(0, size);
		
		return s;
	}
	
	public ByteBuffer getBuffer () {	
		ByteBuffer buf = ByteBuffer.allocate(165);
		
		buf.putLong(matric);
		
		byte[] vNome = nome.getBytes();
        buf.put(vNome);
        
        byte[] vEndereco = endereco.getBytes();
        buf.put(vEndereco);
        
		buf.putShort(idade);
		
		byte[] vCpf = cpf.getBytes();
        buf.put(vCpf);
        
        return buf;
	}

	public long getMatric() {
		return matric;
	}
}