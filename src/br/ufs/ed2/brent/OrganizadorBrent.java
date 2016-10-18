package br.ufs.ed2.brent;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import br.ufs.ed2.sequencial.Aluno;
import br.ufs.ed2.sequencial.IFileOrganizer;

public class OrganizadorBrent implements IFileOrganizer {

	private final long QTD_REGISTROS_MIGRACAO = 10_000_019L;  
	private final long QTD_REGISTROS_APRESENTACAO = 8_478_096L;  
	private final long VALOR_PRIMO = QTD_REGISTROS_MIGRACAO;
	private static final long INEXISTENTE = -1;

	FileChannel channel;
	private RandomAccessFile raf;

	public OrganizadorBrent (String fileName) throws FileNotFoundException {
		File file = new File(fileName);
		raf = new RandomAccessFile(file, "rw");
		channel = raf.getChannel();
	}

	@Override
	public void putReg(Aluno p) {
		long inicioLista = 0;
		int saltosInserirNovoAluno = 0;
		int saltosMoverAlunoExistente = 0;
		long finalSaltos = 0;
		
		// caso base quando Espa�o vazio ou apagado
		try {
			inicioLista = getHash(p.getMatric());
			if (isPosicaoVazia(inicioLista)) {
				// escreve o aluno na posi��o vazia
				channel.write(p.getBuffer(), inicioLista);
			} else {
				saltosInserirNovoAluno = saltosAteNovaPosicao(inicioLista,
						0, this.getIncremento(p.getMatric()));

				// Recupera matricula do aluno na posi��o e calcula quantidade de saltos
				int matriculaAlunoPosAtual = alocarAluno(inicioLista, Aluno.TAMANHO_MATRIC).getInt();
				saltosMoverAlunoExistente = saltosAteNovaPosicao(inicioLista, 0,
						this.getIncremento(matriculaAlunoPosAtual));

				// Melhor n�o mover o registro atual
				if (saltosInserirNovoAluno <= saltosMoverAlunoExistente) {
					finalSaltos = calcularPosicao(p, inicioLista,
							saltosInserirNovoAluno);

					channel.write(p.getBuffer(), finalSaltos);
				}
				else {
					// Insere o novo registro no lugar do atual
					//e move o outro para uma nova posi��o
					Aluno alunoMover = new Aluno(alocarAluno(inicioLista,
							Aluno.TAMANHO_ALUNO));

					// inseri na posi��o do aluno a ser movido
					channel.write(p.getBuffer(), inicioLista);

					finalSaltos = calcularPosicao(alunoMover, inicioLista,
							saltosMoverAlunoExistente);

					// escreve alunoMover na nova posi��o
					channel.write(alunoMover.getBuffer(), finalSaltos);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Aluno getReg(long matric) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Aluno delReg(long matric) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * - Calcula a posi��o que o registro ser� inserido de 
	 * acordo com o incremento, a quantidade de saltos e a posi��o inial.
	 * @param p
	 * @param inicioLista
	 * @param saltosInserirNovoAluno
	 * @return
	 * @throws IOException
	 */
	private long calcularPosicao(Aluno p, long inicioLista,
			int saltosInserirNovoAluno) throws IOException {
		long finalSaltos;
		finalSaltos = inicioLista
				+ (saltosInserirNovoAluno * getIncremento(p
						.getMatric()));
		
		// Verifica se a posi��o � maior que o canal
		finalSaltos = isOverFlow(finalSaltos);
		
		return finalSaltos;
	}
	
	/**
	 * - Se a posi��o for maior que o canal retorna:
	 * 	retorna o resto da divis�o da posi��o pelo tamanho do canal
	 * @param pPosition
	 * @return
	 * @throws IOException
	 */
	private long isOverFlow (long pPosition) throws IOException{
		if (pPosition >= channel.size()) {
			return pPosition % channel.size();
		}
		
		return pPosition;
	}
	
	/**
	 * - Calcula o Hash da Matricula. Hash[chave] = chave mod P onde P � um
	 * valor primo que corresponde ao tamanho da tabela.
	 * @param ppMatricula
	 * @return
	 * @throws IOException
	 */
	private long getHash (long pMatricula) {
		//- Multiplca pela quantidade de bytes que o aluno possui para
		//obter a real posi��o do aluno no array
		return (pMatricula % this.VALOR_PRIMO) * Aluno.TAMANHO_ALUNO;
	}

	/**
	 * - Calcula o Incremento da Matricula. Inc(chave) = (chave mod (P-2)) + 1
	 * onde P � um valor primo que corresponde ao tamanho da tabela.
	 * 
	 * @param ppMatricula
	 * @return
	 */
	private long getIncremento (long pMatric) {
		// usado na migra��o
		return ((pMatric % (this.VALOR_PRIMO - 2)) + 1) * Aluno.TAMANHO_ALUNO;
		
		// usado na apresenta��o
		//return ((pMatricula/QTD_REGISTROS_APRESENTACAO) % QTD_REGISTROS_APRESENTACAO) * Aluno.TAMANHO_ALUNO;
	}


	/**
	 * Aloca uma inst�ncia de Aluno ou parte dos atributos deste em um
	 * ByteBuffer para recuperar os valores gravados
	 * 
	 * @param pos
	 *            Posi��o apartir da qual ira obter os valores do aluno
	 * @param pBuffTamanho
	 *            Tamanho do buffer. Varia a depender do que pretende obter.
	 *            Para matricula, apenas 4, matricula e nome 4 + 50...
	 * @return buff ByteBuffer
	 * @throws IOException
	 */
	private ByteBuffer alocarAluno (long pos, int pBuffTamanho) {
		ByteBuffer buff = ByteBuffer.allocate(pBuffTamanho);
		try {
			this.channel.read(buff, pos);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		buff.flip();
		return buff;
	}

	/**
	 * Calcula a quantidade de saltos at� encontrar uma posi��o livre para
	 * inserir
	 * 
	 * @param pMatricula
	 * @param pPosition
	 * @param qtSaltos
	 * @param pIncremento 
	 * @return quantidade de saltos at� encontrar posi��o livre
	 * @throws IOException 
	 */
	private int saltosAteNovaPosicao (long pPosition, int qtSaltos, long pIncremento) throws IOException {
		// atulisado para quando o salto voltar ao inicio da lista
		long position = pPosition;
		if(pPosition > (channel.size()-1)){
			position = pPosition - channel.size();
		}

		if(isPosicaoVazia(position)){
			return qtSaltos;
		}else{
			return saltosAteNovaPosicao(position + pIncremento, qtSaltos + 1, pIncremento);
		}
	}
	
	/**
	 * - Chama a recurs�o
	 * @param pMatricula
	 * @return
	 * @throws IOException
	 */
	private long getPosition (int pMatricula) throws IOException {
		return getPosition(pMatricula, getHash(pMatricula),
				getIncremento(pMatricula));
	}
	
	/**
	 * - Encontra a posi��o da matricula passada
	 * @param pMatricula
	 * @param pPosition
	 * @param pIncremento
	 * @return
	 * @throws IOException
	 */
	private long getPosition (int pMatricula, long pPosition, long pIncremento) throws IOException {
		// atulisado para quando o salto voltar ao inicio da lista
		long positionAtual = pPosition;
		if(pPosition >= channel.size()){
			positionAtual = pPosition - channel.size();
		}
		
		int matriculaAluno = alocarAluno(positionAtual, Aluno.TAMANHO_MATRIC).getInt();
		if (matriculaAluno == 0) {
			return INEXISTENTE;
		}else if(matriculaAluno == pMatricula){
			return positionAtual;
		}else{
			return getPosition (pMatricula, positionAtual + pIncremento, pIncremento);
		}
	}

	/**
	 * Verifica se na posi��o passada existe um aluno v�lido ou se � vazia
	 * 
	 * @param pPosition
	 *            posi��o do arquivo
	 * @return True se for vazio False se existir aluno v�lido
	 */
	private boolean isPosicaoVazia (long pPosition) {
		// Recupera a matricula do aluno existente na posi��o passada
		int matriculaAluno = alocarAluno(pPosition, Aluno.TAMANHO_MATRIC).getInt();
		if (matriculaAluno == 0 || matriculaAluno == INEXISTENTE) {
			return true;
		}
		
		return false;
	}
}
