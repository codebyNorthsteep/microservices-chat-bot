package org.example.bff;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.HandlerFilterFunction;
import org.springframework.web.servlet.function.HandlerFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

@Component
public class UserFilter implements HandlerFilterFunction<ServerResponse, ServerResponse> {

    @Override
    public ServerResponse filter(ServerRequest request, HandlerFunction<ServerResponse> next) throws Exception {
        if (request.servletRequest().getUserPrincipal() == null) {
            return ServerResponse.status(401).build();
        }
        String username = request.servletRequest().getUserPrincipal().getName();
        ServerRequest modifiedRequest = ServerRequest.from(request)
                .headers(httpHeaders -> {
                    // .set ser till att eventuella headers från klienten raderas
                    // och ersätts helt av gatewayens verifierade användarnamn.
                    httpHeaders.set("X-User-Name", username);
                })
                .build();
        return next.handle(modifiedRequest);
    }
}
