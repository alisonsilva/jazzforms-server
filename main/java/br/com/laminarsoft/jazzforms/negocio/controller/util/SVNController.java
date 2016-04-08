package br.com.laminarsoft.jazzforms.negocio.controller.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringBufferInputStream;
import java.util.Collection;
import java.util.Iterator;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.tmatesoft.svn.core.SVNCommitInfo;
import org.tmatesoft.svn.core.SVNErrorCode;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.SVNProperties;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.ISVNOptions;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNInfo;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNUpdateClient;
import org.tmatesoft.svn.core.wc.SVNWCClient;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

import br.com.laminarsoft.jazzforms.persistencia.model.Projeto;

@Controller
public class SVNController {
	private static SVNController SVN_CONTROLLER;
	public static String USUARIO_DEFAULT;
	public static String SENHA_DEFAULT;
	
	@Autowired private PropertiesServiceController propertiesServiceController;
	
	private JAXBContext jaxbContextProjeto = null;
	private SVNRepository repository;
	private SVNClientManager clientManager;
	private File dirRepo;
	
	public SVNController() {
		setupLibrary();
		try {
			if (propertiesServiceController == null) {
				propertiesServiceController = PropertiesServiceController.getInstance();
			}
			
			dirRepo = new File(propertiesServiceController.getProperty(IProjetoController.SVN_LOCAL_REPO));
			if(!dirRepo.exists()) {
				dirRepo.createNewFile();
			}
			USUARIO_DEFAULT = propertiesServiceController.getProperty(IProjetoController.SVN_REPO_USUARIO);
			SENHA_DEFAULT = propertiesServiceController.getProperty(IProjetoController.SVN_REPO_SENHA);
			repository = SVNRepositoryFactory.create(SVNURL.parseURIEncoded(propertiesServiceController.getProperty(IProjetoController.SVN_REPO_URL)));
			autenticaUsuario(propertiesServiceController.getProperty(IProjetoController.SVN_REPO_USUARIO), propertiesServiceController.getProperty(IProjetoController.SVN_REPO_SENHA));
			jaxbContextProjeto = JAXBContext.newInstance(Projeto.class);
			
		} catch (SVNException e) {
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		} catch(JAXBException e) {		
			e.printStackTrace();
		}
	}

	private void autenticaUsuario(String usuario, String senha) {
		ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(usuario, senha);
		repository.setAuthenticationManager(authManager);
		ISVNOptions options = SVNWCUtil.createDefaultOptions(true);
		clientManager = SVNClientManager.newInstance(options, authManager);
	}
	
    private void setupLibrary() {
        /*
         * For using over http:// and https://
         */
        DAVRepositoryFactory.setup();
        /*
         * For using over svn:// and svn+xxx://
         */
        SVNRepositoryFactoryImpl.setup();
        
        /*
         * For using over file:///
         */
        FSRepositoryFactory.setup();
    }	
	
	public static SVNController getInstance() {
		if (SVN_CONTROLLER == null) {
			SVN_CONTROLLER = new SVNController();
		}
		return SVN_CONTROLLER;
	}
	
	private SVNCommitInfo makeDirectory( SVNURL url , String commitMessage ) throws SVNException {
         return clientManager.getCommitClient( ).doMkDir( new SVNURL[] { url } , commitMessage );
    }
	
	public boolean isVersioned(File wcPath) {
		boolean versioned = true;
		SVNWCClient client = clientManager.getWCClient();
		try {
			client.doInfo(wcPath, SVNRevision.UNDEFINED);
		} catch (SVNException e) {
			final SVNErrorCode errorCode = e.getErrorMessage().getErrorCode();
			if(errorCode == SVNErrorCode.WC_NOT_FILE || errorCode == SVNErrorCode.WC_NOT_DIRECTORY || errorCode == SVNErrorCode.WC_PATH_NOT_FOUND) {
				versioned = false;
			}
		}
		return versioned;
	}
	
	@SuppressWarnings("all")
	public Collection getFileLog(String nomeProjeto) throws SVNException {
		String filePath = propertiesServiceController.getProperty(IProjetoController.SVN_LOCAL_REPO) + "/" + nomeProjeto + ".xml";
		String fileName = propertiesServiceController.getProperty(IProjetoController.SVN_REPO_DIRETORIO_PRINCIPAL) + "/" + nomeProjeto + ".xml";
		File f = new File(filePath);
		this.update(f, SVNRevision.HEAD, true);
		if (!f.exists()) {
			return null;
		}
		SVNInfo info = clientManager.getWCClient().doInfo(new File(filePath), SVNRevision.HEAD);
		Collection coll = repository.getFileRevisions(fileName, null, SVNRevision.BASE.getNumber(), info.getRevision().getNumber());
		return coll;
	}
	
	@SuppressWarnings("all")
	public Projeto checkoutProjeto(String nomeProjeto, String revision) throws SVNException {
		Projeto ret = null;
		String filePath = propertiesServiceController.getProperty(IProjetoController.SVN_LOCAL_REPO) + "/" + nomeProjeto + ".xml";
		String fileName = propertiesServiceController.getProperty(IProjetoController.SVN_REPO_DIRETORIO_PRINCIPAL) + "/" + nomeProjeto + ".xml";

		SVNUpdateClient updateClient = clientManager.getUpdateClient();
		updateClient.setIgnoreExternals(false);
		
		SVNProperties fileProperties = new SVNProperties();
		ByteArrayOutputStream ous;
		SVNNodeKind nodeKind = repository.checkPath(fileName, -1);
		if(nodeKind != SVNNodeKind.NONE && nodeKind != SVNNodeKind.DIR) {
			try {
				ous = new ByteArrayOutputStream();
				repository.getFile(fileName, Long.valueOf(revision), fileProperties, ous);
				Collection logEntries = repository.log(new String[]{""}, null, Long.valueOf(revision), Long.valueOf(revision), true, true);
				Unmarshaller unmarshaller = jaxbContextProjeto.createUnmarshaller();
				ret = (Projeto) unmarshaller.unmarshal(new StringBufferInputStream(ous.toString()));
				for(Iterator entries = logEntries.iterator(); entries.hasNext(); ) {
					SVNLogEntry logEntry = (SVNLogEntry)entries.next();
					String msg = logEntry.getMessage();
					ret.setVersionControllDesc(msg);
				}
			} catch (JAXBException e) {
				e.printStackTrace();
			} 
		}
		return ret;
	}
	
	@SuppressWarnings("deprecation")
	public long update(File wcPath, SVNRevision updateToRevision, boolean isRecursive) throws SVNException {
		SVNUpdateClient updateClient = clientManager.getUpdateClient();
		updateClient.setIgnoreExternals(false);
		return updateClient.doUpdate(wcPath, updateToRevision, isRecursive);
	}
	
	@SuppressWarnings("deprecation")
	public void addEntry(File wcPath) throws SVNException {
		clientManager.getWCClient().doAdd(wcPath, false, false, false, true);
	}
	
	@SuppressWarnings("deprecation")
	public SVNCommitInfo commit(File wcPath, boolean keepLocks, String commitMessage) throws SVNException {
		return clientManager.getCommitClient().doCommit(new File[]{wcPath}, keepLocks, commitMessage, false, true);
	}
}
