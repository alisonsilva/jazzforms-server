package br.com.laminarsoft.jazzforms.persistencia.model.exception;

public interface IMensagensErro {
	public static final int PARAMETRO_NULO_CODE = 1001;
	public static final int PARAMETRO_ID_INVALIDO = 3002;
	public static final String PARAMETRO_NULO = "Parâmetro nâo pode ser nulo";
	public static final String IDENTIFICADOR_VALIDO = "O parâmetro precisa ter um identificador válido";
	public static final int PARAMETRO_QUANTIDADE_ERRADA_CODE = 1002;
	public static final String PARAMETRO_QUANTIDADE_ERRADA_MSG = "Quantidade de parâmetros errada: {0}";

	public static final String EMPRESA_RESPONSAVEL_VAZIA = "1002";
	public static final String CLIENTE_DE_EXEMPLO_VAZIO = "1003";
	public static final String CLIENTE_IDENTIFICADOR_INVALIDO = "1004";
	
	public static final String EMPRESA_IDENTIFICADOR_INVALIDO = "2001";
	public static final String EMPRESA_CAMPOS_OBRIGATORIOS = "2002";
	public static final String EMPRESA_DE_EXEMPLO_VAZIA = "2003";
	public static final String CNPJ_EMPRESA_INVALIDO = "2004";
	public static final String RAMO_EMPRESA_INVALIDO = "2005";
	public static final String COORDENADAS_PASSADAS_INVALIDAS = "2006";
	public static final String EMPRESA_LOGIN_SENHA_INVALIDOS = "2007";
	
	public static final String PRODUTO_CAMPOS_ORIGATORIOS_VAZIOS = "3001";
	public static final String PRODUTO_IDENTIFICADOR_INVALIDO = "3002";
	
	public static final String RAMO_NOME_INVALIDO = "4001";
	public static final String RAMO_PARAMETROS_INVALIDOS = "4002";
	public static final String RAMO_ID_INVALIDO = "4003";
	
	public static final int TAREFA_JA_ATRIBUIDA_CODE = 5001;
	public static final int TAREFA_NAO_PODESER_ATRIBUIDA_CODE = 5002;
	public static final String TAREFA_JA_ATRIBUIDA_MSG = "A tarefa já encontra-se atribuída";
	public static final String TAREFA_NAO_PODESER_ATRIBUIDA_MSG = "A terefa não pode ser atribuída";
	
	public static final int FORMATACAO_INVALIDA_CODE = 11;
	public static final String FORMATACAO_INVALIDA_MSG = "A formatação do campo \"{0}\" está inválida";
	public static final int USUARIO_EXISTENTE_CODE = 10;
	public static final String USUARIO_EXISTENTE_MSG = "Usuário já existente";
	public static final int USUARIO_INEXISTENTE_CODE = 12;
	public static final String USUARIO_INEXISTENTE_MSG = "Usuário inexistente";
	public static final int USUARIO_ATIVO_CODE = 13;
	public static final String USUARIO_ATIVO_MSG = "Usuário está ativo";
	public static final int USUARIO_INATIVO_CODE = 13;
	public static final String USUARIO_INATIVO_MSG = "Usuário está inativo";
	public static final int MUDAR_SENHA_CODE = 14;
	public static final String MUDAR_SENHA_MSG = "É necessário alterar sua senha";
	public static final String USUARIO_CORPORATIVO_MSG = "Não é necessário realizar cadastro. Usuário e senha são os mesmos da rede";
	public static final String USUARIO_LEMBRETE_COROPORATIVO_MSG = "Usuário e senha são os mesmos da rede";
	public static final int USUARIO_CORPORATIVO_CODE = 15;
}
