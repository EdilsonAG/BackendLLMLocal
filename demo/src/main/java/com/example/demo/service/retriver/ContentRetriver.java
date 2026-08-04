package com.example.demo.service.retriver;

@Component
public class ContetRetriver {
    @Bean
    ContentRetriever contentRetriever() throws IOException {
        EmbeddingModel embeddingModel = new AllMiniLmL6V2EmbeddingModel();
        EmbeddingStore<TextSegment> store = new InMemoryEmbeddingStore<>();

        List<Document> docs = new ArrayList<>();
        var resources = new PathMatchingResourcePatternResolver()
                .getResources("classpath:playbooks/*.md");
        for (Resource r : resources) {
            String text = new String(r.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            docs.add(Document.from(text, Metadata.from("file", r.getFilename())));
        }

        EmbeddingStoreIngestor.builder()
                .embeddingStore(store)
                .embeddingModel(embeddingModel)
                // 1 playbook = 1 segmento (não quebrar no meio dos campos obrigatórios)
                .documentSplitter(DocumentSplitters.recursive(2000, 0))
                .build()
                .ingest(docs);

        return EmbeddingStoreContentRetriever.builder()
                .embeddingStore(store)
                .embeddingModel(embeddingModel)
                .maxResults(2)
                .minScore(0.4)
                .build();
    }
}