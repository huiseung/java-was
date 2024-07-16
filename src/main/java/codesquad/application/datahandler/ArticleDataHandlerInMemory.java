package codesquad.application.datahandler;

import codesquad.application.domain.Article;
import codesquad.webserver.annotation.DataHandler;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@DataHandler("ArticleDataHandlerInMemory")
public class ArticleDataHandlerInMemory implements ArticleDataHandler{
    private Map<String, Article> store = new ConcurrentHashMap<>();

    @Override
    public void insert(Article article) {
        String key = UUID.randomUUID().toString();
        article.setId(key);
        store.put(key, article);
    }

    @Override
    public List<Article> getAll(){
        return store.values().stream().toList();
    }
}
