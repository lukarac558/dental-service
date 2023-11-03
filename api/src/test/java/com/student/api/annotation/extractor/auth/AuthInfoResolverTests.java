package com.student.api.annotation.extractor.auth;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.NativeWebRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AuthInfoResolverTests {

    String tokenValid = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJsMUhWZE1KaDJnZFJEWHJleWhPai1QSURmS2dzRGowdU4tS1lmeTFwX2RBIn0.eyJleHAiOjE2OTkwMjU5OTQsImlhdCI6MTY5OTAyNTY5NCwiYXV0aF90aW1lIjoxNjk5MDI1Njk0LCJqdGkiOiIxZWQzOTc1ZS00MzdjLTQ5OWQtOTEzNC0xNWE4YTAzZDc5ZTEiLCJpc3MiOiJodHRwOi8vbG9jYWxob3N0OjgxODEvcmVhbG1zL2RlbnRhbC1jbGllbnQiLCJhdWQiOiJhY2NvdW50Iiwic3ViIjoiZjAwMjg5OTgtZTNkNi00N2Q0LTkxYzAtNTMyYTE5ZjY3NTQ5IiwidHlwIjoiQmVhcmVyIiwiYXpwIjoiZGVudGFsLXNlcnZpY2UiLCJzZXNzaW9uX3N0YXRlIjoiYzUwOGJhMjEtMTJjOC00MDYwLTgyOGMtN2Y5YzlkODgxZDU1IiwiYWNyIjoiMSIsImFsbG93ZWQtb3JpZ2lucyI6WyJodHRwOi8vbG9jYWxob3N0OjgwODAiLCJodHRwOi8vbG9jYWxob3N0OjQyMDAiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbIm9mZmxpbmVfYWNjZXNzIiwidW1hX2F1dGhvcml6YXRpb24iXX0sInJlc291cmNlX2FjY2VzcyI6eyJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50IiwibWFuYWdlLWFjY291bnQtbGlua3MiLCJ2aWV3LXByb2ZpbGUiXX0sImRlbnRhbC1zZXJ2aWNlIjp7InJvbGVzIjpbImRvY3RvciJdfX0sInNjb3BlIjoicHJvZmlsZSBlbWFpbCIsInNpZCI6ImM1MDhiYTIxLTEyYzgtNDA2MC04MjhjLTdmOWM5ZDg4MWQ1NSIsImVtYWlsX3ZlcmlmaWVkIjpmYWxzZSwicHJlZmVycmVkX3VzZXJuYW1lIjoiZGVudGlzdEBkZW50aXN0LmRlbnRpc3QiLCJlbWFpbCI6ImRlbnRpc3RAZGVudGlzdC5kZW50aXN0In0.gF9OIGsMP5zZj33mepPBkQkiWq661RGwh5xfWaw3y5aXBSmDE0PHYjuyhBAJ6VAuRwWtQYL6TkbPTSlvpITwbRY_cgDos7yMc4v1Ve27gmSdyIcm4_3u6OaScN3jzEyTYAQQrIrtNCu2V2cyhW4aBmXkyO4LtsPsK6TjPw7EjxFch1gpsZXXJ19hGlMwV-Tj84d9ng4JweoXuc4LfmrejvGyCYbby09T84Z3M9zz4Fk_XFdHTL2DMMlNqJrrBZvtqbhyA-MwM9KeuBP_rYzk94sV0-GbRub1Xiq5Mxu-LG6hg9FoeXOL02bZJlalFJ4VkyY7k83LI_Rb0vgvG1O6Pw";

    @Test
    void shouldExtractFromToken() throws Exception {
        AuthInfoResolver authInfoResolver = new AuthInfoResolver();
        NativeWebRequest webRequest = Mockito.mock(NativeWebRequest.class);
        HttpServletRequest servletRequest = Mockito.mock(HttpServletRequest.class);
        Mockito.when(webRequest.getNativeRequest()).thenReturn(servletRequest);
        Mockito.when(servletRequest.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(buildToken(tokenValid));

        Info info = authInfoResolver.resolveArgument(null, null, webRequest, null);

        assertEquals("dentist@dentist.dentist", info.getEmail());
    }

    private String buildToken(String token) {
        return "Bearer " + token;
    }
}
