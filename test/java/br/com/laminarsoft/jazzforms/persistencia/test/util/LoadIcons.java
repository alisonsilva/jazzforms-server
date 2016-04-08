package br.com.laminarsoft.jazzforms.persistencia.test.util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import junit.framework.Assert;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.hibernate.validator.AssertTrue;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import br.com.laminarsoft.jazzforms.persistencia.dao.IconDao;
import br.com.laminarsoft.jazzforms.persistencia.model.ComponentType;
import br.com.laminarsoft.jazzforms.persistencia.model.TipoEvento;
import br.com.laminarsoft.jazzforms.persistencia.model.senchawrapper.Icon;
import br.com.laminarsoft.jazzforms.persistencia.test.TesteBase;

@SuppressWarnings("all")
public class LoadIcons extends TesteBase {
	private POIFSFileSystem file;

	@Before
	public void iniciaVariaveis() {
		try {
			InputStream is = LoadEventos.class.getResourceAsStream("/DadosBanco.xls");
			file = new POIFSFileSystem(is);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	@Test
	public void executaCargaTipoEvento() {
		int iter = 0;
		Icon icon = null;
		try {
			Map<Integer, TipoEvento> tipos = new HashMap<Integer, TipoEvento>();
			Map<Integer, ComponentType> cmpTypes = new HashMap<Integer, ComponentType>();
			
			/** Create a workbook using the File System **/
			HSSFWorkbook workBook = new HSSFWorkbook(file);

			/** Get the first sheet from workbook **/
			HSSFSheet tipoEventoSheet = workBook.getSheet("ICON");

			/** We now need something to iterate through the cells. **/
			Iterator rowIter = tipoEventoSheet.rowIterator();

			while (rowIter.hasNext()) {
				HSSFRow myRow = (HSSFRow) rowIter.next();
				if (iter > 0) {
					Iterator cellIter = myRow.cellIterator();
					if (cellIter.hasNext()) {
						icon = new Icon();
						cellIter.next();
						icon.setNome(((HSSFCell)cellIter.next()).toString());
						String nomeArquivo24 = ((HSSFCell)cellIter.next()).getStringCellValue();
						String nomeArquivo16 = ((HSSFCell)cellIter.next()).getStringCellValue();
						
						if (nomeArquivo24.trim().length() == 0 && nomeArquivo16.trim().length() == 0) {
							continue;
						}
						
						InputStream in24 = new BufferedInputStream(IconDao.class.getResourceAsStream("/" + nomeArquivo24));
						byte[] in = new byte[1024];
						while((in24.read(in)) > 0) {
							byte[] pict24 = icon.getIcon24();
							if (pict24 == null) {
								icon.setIcon24(in);
							} else {
								String strbytes = new String(in);
								String strbytes24 = new String(icon.getIcon24());
								String concat = strbytes24 + strbytes;
								icon.setIcon24(concat.getBytes());
							}
						}
						in24.close();
						
						InputStream in16 = new BufferedInputStream(IconDao.class.getResourceAsStream("/" + nomeArquivo16));
						in = new byte[1024];
						while((in16.read(in)) > 0) {
							byte[] pict16 = icon.getIcon16();
							if(pict16 == null) {
								icon.setIcon16(in);
							} else {
								String strbytes = new String(in);
								String strbytes16 = new String(icon.getIcon16());
								String concat = strbytes16 + strbytes;
								icon.setIcon16(concat.getBytes());
							}
						}						
						in16.close();
						iconDao.persist(icon);
					}
				}
				iter++;
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.print(iter + " - " + icon);
			Assert.assertTrue(false);
		}
	}
	
}
