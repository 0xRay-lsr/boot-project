package cn.aps.boot.web.handler;

import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.boot.web.server.ErrorPageRegistry;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * @Author : lishirui
 */
@Component
public class ErrorRegister implements ErrorPageRegistrar {

    @Override
    public void registerErrorPages(ErrorPageRegistry registry) {
        ErrorPage err400 = new ErrorPage(HttpStatus.BAD_REQUEST, "/error");
        ErrorPage err404 = new ErrorPage(HttpStatus.NOT_FOUND, "/error");
        ErrorPage err500 = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error");
        registry.addErrorPages(err400, err404, err500);
    }

}