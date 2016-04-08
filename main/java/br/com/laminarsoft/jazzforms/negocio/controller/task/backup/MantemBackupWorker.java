package br.com.laminarsoft.jazzforms.negocio.controller.task.backup;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.laminarsoft.jazzforms.logging.types.LoggingErrorMSG;
import br.com.laminarsoft.jazzforms.negocio.controller.util.LogController;
import br.com.laminarsoft.jazzforms.negocio.controller.util.PropertiesServiceController;

@Component("mantemBackupWorker")
@SuppressWarnings("all")
public class MantemBackupWorker implements Runnable {
	
	@Autowired private PropertiesServiceController propertiesServiceController;
	@Autowired private LogController logController;
	
	@Override
	public void run() {
		if(propertiesServiceController == null) {
			propertiesServiceController = PropertiesServiceController.getInstance();
		}
		String dirBaseBackup = propertiesServiceController.getProperty("backup.banco.jazzit.diretoriobase");
		String nomesArquivos = propertiesServiceController.getProperty("backup.banco.jazzit.nomearquivo");
		String usuario = propertiesServiceController.getProperty("backup.banco.jazzit.usuario");
		String senha = propertiesServiceController.getProperty("backup.banco.jazzit.password");
		String comando = propertiesServiceController.getProperty("backup.banco.jazzit.comando");
		
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
			String comandoFinal = "\"" + comando + "\" -u " + usuario + " -p" + senha + " ";
			StringTokenizer strtok = new StringTokenizer(nomesArquivos, ";");
			
			while (strtok.hasMoreTokens()) {
				String nome = strtok.nextToken();
				String destinoRaiz = dirBaseBackup + "/" + nome + "_" + sdf.format(new Date());
				String destino = destinoRaiz + ".sql";
				final Process p = Runtime.getRuntime().exec(comandoFinal + " " + nome + " -r " + destino);
				
				new Thread(new Runnable() {
				    public void run() {
				        BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
				        String line = null; 

				        try {
				            while ((line = input.readLine()) != null) {
				                System.out.println(line);
				            }
				        } catch (IOException e) {
				            e.printStackTrace();
				        }
				    }
				}).start();

				p.waitFor();
				
				byte[] buffer = new byte[1024];
				if(p.exitValue() == 0) {//backup executado com sucesso
					FileOutputStream fos = new FileOutputStream(destinoRaiz + ".zip");
					ZipOutputStream zos = new ZipOutputStream(fos);
					ZipEntry ze = new ZipEntry(destino);
					zos.putNextEntry(ze);
					
					FileInputStream in = new FileInputStream(destino);
					int len;
					while((len = in.read(buffer)) > 0) {
						zos.write(buffer, 0, len);
					}					
					in.close();
					zos.closeEntry();
					zos.close();
					File fin = new File(destino);
					fin.delete();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			sendMessageError("Backup", "erro: " + e.getMessage(), "");
		}
	}

	
	private void sendMessageError(String servicoChamado, String msgErro, String parametros) {
		LoggingErrorMSG erro = new LoggingErrorMSG();
		erro.setDhChamada(new Date()); 
		erro.servicoChamado = servicoChamado;
		erro.mensagemErro = msgErro;
		erro.parametrosChamada = parametros; 
				
		logController.enviaMensagemErro(erro);
		
	}	
}
