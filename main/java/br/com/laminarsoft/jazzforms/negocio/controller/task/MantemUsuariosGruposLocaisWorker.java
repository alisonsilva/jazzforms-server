package br.com.laminarsoft.jazzforms.negocio.controller.task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sf.ehcache.Ehcache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import br.com.laminarsoft.jazzforms.persistencia.dao.UsuarioDao;
import br.com.laminarsoft.jazzforms.persistencia.model.wrapper.LdapUsuarioVO;

@Component("mantemUsuariosGruposLocaisWorker")
@SuppressWarnings("all")
public class MantemUsuariosGruposLocaisWorker implements Runnable {
	private static final String NOME_CACHE_USRUPDT = "usuarios_para_update";
	
	@Autowired private UsuarioDao usuarioDao;
	@Autowired private CacheManager cacheManager;
	
	@Override
	public void run() {
		Cache cache = cacheManager.getCache(NOME_CACHE_USRUPDT);
		Ehcache ehcache = (Ehcache)cache.getNativeCache();
		synchronized (this) {
			List chaves = ehcache.getKeys();
			for (int idx = 0; idx < chaves.size(); idx++) {
				String chave = (String) chaves.get(idx);
				LdapUsuarioVO ldapusr = (LdapUsuarioVO) cache.get(chave).get();
				cache.evict(chave);
				usuarioDao.atualizaRelacionamentoUsuario(ldapusr);
			}
		}
	}

}
