package br.com.facens.atividade4.domain;

/**
 * Consultor de Cursos com IA (AI Course Advisor).
 * Interface de serviço de IA que ajuda alunos com informações sobre cursos,
 * assinaturas e gamificação.
 *
 * Esta interface será implementada pelo LangChain4j usando o modelo OpenAI configurado.
 */
public interface AICourseAdvisor {

    /**
     * Conversa com o consultor de IA.
     *
     * @param userMessage mensagem do usuário
     * @return resposta da IA
     */
    String chat(String userMessage);
}
