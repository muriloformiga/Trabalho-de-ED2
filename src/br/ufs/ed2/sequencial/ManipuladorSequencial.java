package br.ufs.ed2.sequencial;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class ManipuladorSequencial implements IFileOrganizer {

	FileChannel channel;

	public ManipuladorSequencial (String fileName) throws FileNotFoundException {
		File file = new File(fileName);
		RandomAccessFile raf = new RandomAccessFile(file, "rw");
		channel = raf.getChannel();
	}

	@Override
	public void putReg (Aluno p) {

		try {
			//channel.write((ByteBuffer) p.getBuffer().position(0), channel.size());

			ByteBuffer byteBuffer = ByteBuffer.allocate(p.getBuffer().capacity());
			//System.out.println(channel.size());

			channel.position(0);
			int x = 0;
			while (channel.position() < channel.size()) {
				channel.read(byteBuffer);
				byteBuffer.flip();
				if (p.getBuffer().getLong(0) > byteBuffer.getLong(0)) {
					System.out.println("entrou!");
					x = 1;
					break;
				}
			}
			
			ByteBuffer b2 = ByteBuffer.allocate(165);
			while (b2 != null) {
				
				channel.read(b2, channel.size() - 165);
				channel.write(b2);
			}
			
			//if (x == 1)
				//channel.position(channel.position() - 165);
			channel.write((ByteBuffer) p.getBuffer().position(0));
			System.out.println(channel.position() + " :: " + channel.size() + " :: " + 
					p.getBuffer().getLong(0) + " :: " + byteBuffer.getLong(0) + byteBuffer);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public Aluno getReg(long matric) {
		try {

			channel.position(0);
			while (channel.position() < channel.size()){
				ByteBuffer buf = ByteBuffer.allocate(165);
				channel.read(buf);
				buf.flip();
				Aluno a = new Aluno(buf);
				System.out.println(a.getMatric());
				if(a.getMatric() == matric){
					return a;
				}
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}// preenche ele para depois ler

		//		try {
		//
		//			channel.position(0);
		//			while (channel.position() < channel.size()){
		//				ByteBuffer buf = ByteBuffer.allocate(Aluno.LENGTH);
		//				channel.read(buf);
		//				buf.flip();
		//				Aluno a = new Aluno(buf);
		//				System.out.println(a.getBuffer().getLong(0));
		//				if(a.getBuffer().getLong(0) == matric){
		//					return a;
		//				}
		//			}
		//
		//
		//		} catch (IOException e) {
		//			// TODO Auto-generated catch block
		//			e.printStackTrace();
		//		}// preenche ele para depois ler
		//
		//		return null;

		return null;
	}

	@Override
	public Aluno delReg(long matric) {
		// TODO Auto-generated method stub
		return null;
	}
}
