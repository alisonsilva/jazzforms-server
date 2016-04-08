package br.com.laminarsoft.jazzforms.persistencia.test.util;

import static org.junit.Assert.assertTrue;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import br.com.laminarsoft.jazzforms.persistencia.model.ComponentType;
import br.com.laminarsoft.jazzforms.persistencia.model.TipoEvento;
import br.com.laminarsoft.jazzforms.persistencia.test.TesteBase;


@SuppressWarnings("all")
public class LoadEventos extends TesteBase {

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

	
	@SuppressWarnings("all")
	@Test
	public void executaCargaTipoEvento() {
		try {
			Map<Integer, TipoEvento> tipos = new HashMap<Integer, TipoEvento>();
			Map<Integer, ComponentType> cmpTypes = new HashMap<Integer, ComponentType>();
			
			/** Create a workbook using the File System **/
			HSSFWorkbook workBook = new HSSFWorkbook(file);

			/** Get the first sheet from workbook **/
			HSSFSheet tipoEventoSheet = workBook.getSheet("TIPO_EVENTO");
			HSSFSheet cmpTypesSheet = workBook.getSheet("COMPONENT_TYPE");

			/** We now need something to iterate through the cells. **/
			Iterator rowIter = tipoEventoSheet.rowIterator();
			Iterator rowIterCMPTypes = cmpTypesSheet.rowIterator();

			int iter = 0;
			while (rowIterCMPTypes.hasNext()) {
				HSSFRow myRow = (HSSFRow) rowIterCMPTypes.next();
				if (iter > 0) {
					Iterator cellIter = myRow.cellIterator();
					if (cellIter.hasNext()) {
						ComponentType ctype = new ComponentType();

						Integer id = (new Float(((HSSFCell)cellIter.next()).getNumericCellValue())).intValue();
						if (id == 0) {
							break;
						}

						HSSFCell myCell = (HSSFCell) cellIter.next();
						ctype.setNomeComponent(myCell.toString());

						myCell = (HSSFCell) cellIter.next();
						ctype.setDescricao(myCell.toString());
						ctype.setId(componentTypeDao.persist(ctype));
						cmpTypes.put(id, ctype);						
					}
				}
				iter++;
			}			
			
			iter = 0;
			while (rowIter.hasNext()) {
				HSSFRow myRow = (HSSFRow) rowIter.next();
				if (iter > 0) {
					Iterator cellIter = myRow.cellIterator();
					if (cellIter.hasNext()) {
						TipoEvento tipoEvento = new TipoEvento();

						Integer id = (new Float(((HSSFCell)cellIter.next()).getNumericCellValue())).intValue();
						if (id == 0) {
							break;
						}

						HSSFCell myCell = (HSSFCell)cellIter.next();
						ComponentType ct = cmpTypes.get(new Float(myCell.getNumericCellValue()).intValue());
						tipoEvento.setComponent(ct);
						
						myCell = (HSSFCell) cellIter.next();
						tipoEvento.setNome(myCell.toString());

						myCell = (HSSFCell) cellIter.next();
						tipoEvento.setDescricao(myCell.toString());

						myCell = (HSSFCell) cellIter.next();
						tipoEvento.setAssinatura(myCell.toString());
						
						tipos.put(id, tipoEvento);

					}
				}
				iter++;
			}

//			putEventosInTipoEventos(tipos);
			
			TipoEvento[] tpEventoArray = tipos.values().toArray(new TipoEvento[tipos.size()]);
			for(int i = 0; i < tpEventoArray.length; i++) {
				tipoEventoDao.persist(tpEventoArray[i]);
			}
			
			assertTrue(true);
		} catch (Exception e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}

	
}
