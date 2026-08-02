package com.example.demo;

import java.util.List;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentParser;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.document.parser.apache.tika.ApacheTikaDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import org.springframework.stereotype.Component;

@Component
public class Treinamento {

    private final EmbeddingModel embeddingModel;
    private final EmbeddingStore<TextSegment> embeddingStore;

    // Spring injeta os dois beans automaticamente
    public Treinamento(EmbeddingModel embeddingModel, EmbeddingStore<TextSegment> embeddingStore) {
        this.embeddingModel = embeddingModel;
        this.embeddingStore = embeddingStore;
    }

    public void treinar() {
         System.out.println("começando treinamento");
    DocumentParser parser = new ApacheTikaDocumentParser();

    List<Document> documents = FileSystemDocumentLoader.loadDocuments(
        "C:\\Users\\raul\\Documents\\Amino\\Amino\\Amino - Suinos", parser
    );
    System.out.println("Arquivos encontrados: " + documents.size());

    DocumentSplitter splitter = DocumentSplitters.recursive(500, 50);
    List<TextSegment> segments = splitter.splitAll(documents);
    System.out.println("Chunks gerados: " + segments.size());

    int loteSize = 20;
    for (int i = 0; i < segments.size(); i += loteSize) {
        List<TextSegment> lote = segments.subList(i, Math.min(i + loteSize, segments.size()));
        List<Embedding> embeddings = embeddingModel.embedAll(lote).content();
        embeddingStore.addAll(embeddings, lote);
        System.out.println("Processado lote " + (i / loteSize + 1) + " de " + (segments.size() / loteSize + 1));
    }

    System.out.println("Indexados " + segments.size() + " trechos no total.");
    }
}