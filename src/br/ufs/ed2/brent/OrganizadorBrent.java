package br.ufs.ed2.brent;
///
import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

import br.ufs.ed2.sequencial.Aluno;
import br.ufs.ed2.sequencial.IFileOrganizer;

public class OrganizadorBrent implements IFileOrganizer {
	
	FileChannel channel;
	private RandomAccessFile raf;
	
    public OrganizadorBrent (String fileName) throws FileNotFoundException {
          File file = new File(fileName);
          raf = new RandomAccessFile(file, "rw");
          channel = raf.getChannel();
    }
	
	
	@Override
	public void putReg(Aluno p) {
		// TODO Auto-generated method stub

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
}
