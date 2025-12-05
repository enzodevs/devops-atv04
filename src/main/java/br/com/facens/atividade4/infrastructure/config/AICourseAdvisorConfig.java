package br.com.facens.atividade4.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;

/**
 * Configuração do LangChain4j com OpenAI.
 * Cria o bean ChatLanguageModel para uso nos serviços de IA.
 */
@Configuration
public class AICourseAdvisorConfig {

    @Bean
    public ChatLanguageModel chatLanguageModel(
            @Value("${openai.api.key:${OPENAI_API_KEY:}}") String apiKey,
            @Value("${openai.model.name:gpt-4o-mini}") String modelName,
            @Value("${openai.temperature:0.7}") Double temperature
    ) {
        return OpenAiChatModel.builder()
                .apiKey(apiKey)
                .modelName(modelName)
                .temperature(temperature)
                .timeout(java.time.Duration.ofSeconds(60))
                .build();
    }
}
