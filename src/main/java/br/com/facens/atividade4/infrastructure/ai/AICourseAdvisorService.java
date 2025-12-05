package br.com.facens.atividade4.infrastructure.ai;

import org.springframework.stereotype.Service;

import br.com.facens.atividade4.domain.AICourseAdvisor;
import dev.langchain4j.model.chat.ChatLanguageModel;

/**
 * Implementação do AI Course Advisor usando LangChain4j e OpenAI.
 * Fornece suporte inteligente para alunos sobre cursos, assinaturas e gamificação.
 */
@Service
public class AICourseAdvisorService implements AICourseAdvisor {

    private final ChatLanguageModel model;
    private final AlunoTools alunoTools;

    private static final String SYSTEM_PROMPT = """
        Você é um consultor educacional especializado em ajudar alunos da plataforma de cursos.

        Suas responsabilidades incluem:
        - Responder perguntas sobre cursos disponíveis
        - Explicar os tipos de assinatura (Básica: 5 cursos iniciais, Premium: 10 cursos iniciais)
        - Orientar sobre o sistema de gamificação e bônus de cursos
        - Buscar informações específicas de alunos quando necessário
        - Dar recomendações personalizadas baseadas no perfil do aluno

        Seja sempre educado, claro e objetivo nas respostas.
        Quando não tiver certeza sobre algo, seja honesto e não invente informações.
        """;

    public AICourseAdvisorService(ChatLanguageModel model, AlunoTools alunoTools) {
        this.model = model;
        this.alunoTools = alunoTools;
    }

    @Override
    public String chat(String userMessage) {
        String enrichedPrompt = buildEnrichedPrompt(userMessage);
        return model.generate(enrichedPrompt);
    }

    private String buildEnrichedPrompt(String userMessage) {
        StringBuilder prompt = new StringBuilder();
        prompt.append(SYSTEM_PROMPT).append("\n\n");

        if (userMessage.toLowerCase().contains("aluno") && userMessage.matches(".*\\b\\d+\\b.*")) {
            String alunoId = extractAlunoId(userMessage);
            if (alunoId != null) {
                String alunoInfo = alunoTools.buscarInformacoesAluno(Long.parseLong(alunoId));
                prompt.append("Contexto do Aluno:\n").append(alunoInfo).append("\n\n");
            }
        }

        if (userMessage.toLowerCase().contains("assinatura") || userMessage.toLowerCase().contains("plano")) {
            String tiposInfo = alunoTools.explicarTiposAssinatura();
            prompt.append("Informações sobre Assinaturas:\n").append(tiposInfo).append("\n\n");
        }

        if (userMessage.toLowerCase().contains("gamificação") || userMessage.toLowerCase().contains("bônus")) {
            String gamificationInfo = alunoTools.explicarGamificacao();
            prompt.append("Informações sobre Gamificação:\n").append(gamificationInfo).append("\n\n");
        }

        prompt.append("Pergunta do usuário: ").append(userMessage);

        return prompt.toString();
    }

    private String extractAlunoId(String message) {
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("\\b(\\d+)\\b");
        java.util.regex.Matcher matcher = pattern.matcher(message);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
}
