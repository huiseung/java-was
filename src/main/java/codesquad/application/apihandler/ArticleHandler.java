package codesquad.application.apihandler;

import codesquad.application.datahandler.ArticleDataHandler;
import codesquad.application.domain.Article;
import codesquad.webserver.annotation.ApiHandler;
import codesquad.webserver.annotation.RequestMapping;
import codesquad.webserver.annotation.Specify;
import codesquad.webserver.http.HttpMethod;
import codesquad.webserver.http.HttpRequest;
import codesquad.webserver.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApiHandler
public class ArticleHandler {
    private final Logger log = LoggerFactory.getLogger(ArticleHandler.class);
    private final ArticleDataHandler articleDb;

    public ArticleHandler(
            @Specify("ArticleDataHandlerInMemory") ArticleDataHandler articleDb) {
        this.articleDb = articleDb;
    }

    @RequestMapping(method = HttpMethod.POST, path = "/write")
    public HttpResponse write(HttpRequest request) {
        String title = (String) request.getHttpBody().get("title");
        String content = (String) request.getHttpBody().get("content");
        log.debug("article title:" + title + ", content:" + content);
        Article article = new Article(title, content);
        articleDb.insert(article);
        log.debug("article: "+articleDb.getAll());
        return HttpResponse.redirect("/index");
    }
}
