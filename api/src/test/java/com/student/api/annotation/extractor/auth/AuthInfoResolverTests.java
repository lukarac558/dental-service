package com.student.api.annotation.extractor.auth;

import com.student.api.dto.common.enums.Role;
import com.student.api.exception.TokenInfoExtractionException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AuthInfoResolverTests {

    String doctorToken = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJsMUhWZE1KaDJnZFJEWHJleWhPai1QSURmS2dzRGowdU4tS1lmeTFwX2RBIn0.eyJleHAiOjE2OTkwMjU5OTQsImlhdCI6MTY5OTAyNTY5NCwiYXV0aF90aW1lIjoxNjk5MDI1Njk0LCJqdGkiOiIxZWQzOTc1ZS00MzdjLTQ5OWQtOTEzNC0xNWE4YTAzZDc5ZTEiLCJpc3MiOiJodHRwOi8vbG9jYWxob3N0OjgxODEvcmVhbG1zL2RlbnRhbC1jbGllbnQiLCJhdWQiOiJhY2NvdW50Iiwic3ViIjoiZjAwMjg5OTgtZTNkNi00N2Q0LTkxYzAtNTMyYTE5ZjY3NTQ5IiwidHlwIjoiQmVhcmVyIiwiYXpwIjoiZGVudGFsLXNlcnZpY2UiLCJzZXNzaW9uX3N0YXRlIjoiYzUwOGJhMjEtMTJjOC00MDYwLTgyOGMtN2Y5YzlkODgxZDU1IiwiYWNyIjoiMSIsImFsbG93ZWQtb3JpZ2lucyI6WyJodHRwOi8vbG9jYWxob3N0OjgwODAiLCJodHRwOi8vbG9jYWxob3N0OjQyMDAiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbIm9mZmxpbmVfYWNjZXNzIiwidW1hX2F1dGhvcml6YXRpb24iXX0sInJlc291cmNlX2FjY2VzcyI6eyJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50IiwibWFuYWdlLWFjY291bnQtbGlua3MiLCJ2aWV3LXByb2ZpbGUiXX0sImRlbnRhbC1zZXJ2aWNlIjp7InJvbGVzIjpbImRvY3RvciJdfX0sInNjb3BlIjoicHJvZmlsZSBlbWFpbCIsInNpZCI6ImM1MDhiYTIxLTEyYzgtNDA2MC04MjhjLTdmOWM5ZDg4MWQ1NSIsImVtYWlsX3ZlcmlmaWVkIjpmYWxzZSwicHJlZmVycmVkX3VzZXJuYW1lIjoiZGVudGlzdEBkZW50aXN0LmRlbnRpc3QiLCJlbWFpbCI6ImRlbnRpc3RAZGVudGlzdC5kZW50aXN0In0.gF9OIGsMP5zZj33mepPBkQkiWq661RGwh5xfWaw3y5aXBSmDE0PHYjuyhBAJ6VAuRwWtQYL6TkbPTSlvpITwbRY_cgDos7yMc4v1Ve27gmSdyIcm4_3u6OaScN3jzEyTYAQQrIrtNCu2V2cyhW4aBmXkyO4LtsPsK6TjPw7EjxFch1gpsZXXJ19hGlMwV-Tj84d9ng4JweoXuc4LfmrejvGyCYbby09T84Z3M9zz4Fk_XFdHTL2DMMlNqJrrBZvtqbhyA-MwM9KeuBP_rYzk94sV0-GbRub1Xiq5Mxu-LG6hg9FoeXOL02bZJlalFJ4VkyY7k83LI_Rb0vgvG1O6Pw";
    String patientToken = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6ImwxSFZkTUpoMmdkUkRYcmV5aE9qLVBJRGZLZ3NEajB1Ti1LWWZ5MXBfZEEifQ.eyJleHAiOjE2OTkwMjU5OTQsImlhdCI6MTY5OTAyNTY5NCwiYXV0aF90aW1lIjoxNjk5MDI1Njk0LCJqdGkiOiIxZWQzOTc1ZS00MzdjLTQ5OWQtOTEzNC0xNWE4YTAzZDc5ZTEiLCJpc3MiOiJodHRwOi8vbG9jYWxob3N0OjgxODEvcmVhbG1zL2RlbnRhbC1jbGllbnQiLCJhdWQiOiJhY2NvdW50Iiwic3ViIjoiZjAwMjg5OTgtZTNkNi00N2Q0LTkxYzAtNTMyYTE5ZjY3NTQ5IiwidHlwIjoiQmVhcmVyIiwiYXpwIjoiZGVudGFsLXNlcnZpY2UiLCJzZXNzaW9uX3N0YXRlIjoiYzUwOGJhMjEtMTJjOC00MDYwLTgyOGMtN2Y5YzlkODgxZDU1IiwiYWNyIjoiMSIsImFsbG93ZWQtb3JpZ2lucyI6WyJodHRwOi8vbG9jYWxob3N0OjgwODAiLCJodHRwOi8vbG9jYWxob3N0OjQyMDAiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbIm9mZmxpbmVfYWNjZXNzIiwidW1hX2F1dGhvcml6YXRpb24iXX0sInJlc291cmNlX2FjY2VzcyI6eyJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50IiwibWFuYWdlLWFjY291bnQtbGlua3MiLCJ2aWV3LXByb2ZpbGUiXX0sImRlbnRhbC1zZXJ2aWNlIjp7InJvbGVzIjpbInBhdGllbnQiXX19LCJzY29wZSI6InByb2ZpbGUgZW1haWwiLCJzaWQiOiJjNTA4YmEyMS0xMmM4LTQwNjAtODI4Yy03ZjljOWQ4ODFkNTUiLCJlbWFpbF92ZXJpZmllZCI6ZmFsc2UsInByZWZlcnJlZF91c2VybmFtZSI6ImRlbnRpc3RAZGVudGlzdC5kZW50aXN0IiwiZW1haWwiOiJwYXRpZW50QHBhdGllbnQucGF0aWVudCJ9.uZslFIsjBXGDjyN3WiJeEmYdZl8SlTFwAd3ipqiYZr3RX9zHeld3LENgzqRBjrpALJP1Qaj4wKOKsOf_rTTtI8AjZPgY4bA6Lver9902j0yoQkZadkv5xWoi-ObZ4yUV9FeMeEn7U1iJrSptXAHh8CWe5MHmMwAYX0kmHZCcMMu4omJGZIfcgWWGcQuE6zbacNhnCStHchfEE3Wz9LdpPL1_OaV1CKJubH_VCXIZ9KS-nwhaaci_NnogjauzbGdrAH4PUw6qf_Yaq57WQuLzbPXmge98clMtwCjTBmSLiPHVPZOYOHoSYhBZPjWS0SU2K5FXBMdYDKo2DK5XK0vQiA";
    String doctorAndPatientToken = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6ImwxSFZkTUpoMmdkUkRYcmV5aE9qLVBJRGZLZ3NEajB1Ti1LWWZ5MXBfZEEifQ.eyJleHAiOjE2OTkwMjU5OTQsImlhdCI6MTY5OTAyNTY5NCwiYXV0aF90aW1lIjoxNjk5MDI1Njk0LCJqdGkiOiIxZWQzOTc1ZS00MzdjLTQ5OWQtOTEzNC0xNWE4YTAzZDc5ZTEiLCJpc3MiOiJodHRwOi8vbG9jYWxob3N0OjgxODEvcmVhbG1zL2RlbnRhbC1jbGllbnQiLCJhdWQiOiJhY2NvdW50Iiwic3ViIjoiZjAwMjg5OTgtZTNkNi00N2Q0LTkxYzAtNTMyYTE5ZjY3NTQ5IiwidHlwIjoiQmVhcmVyIiwiYXpwIjoiZGVudGFsLXNlcnZpY2UiLCJzZXNzaW9uX3N0YXRlIjoiYzUwOGJhMjEtMTJjOC00MDYwLTgyOGMtN2Y5YzlkODgxZDU1IiwiYWNyIjoiMSIsImFsbG93ZWQtb3JpZ2lucyI6WyJodHRwOi8vbG9jYWxob3N0OjgwODAiLCJodHRwOi8vbG9jYWxob3N0OjQyMDAiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbIm9mZmxpbmVfYWNjZXNzIiwidW1hX2F1dGhvcml6YXRpb24iXX0sInJlc291cmNlX2FjY2VzcyI6eyJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50IiwibWFuYWdlLWFjY291bnQtbGlua3MiLCJ2aWV3LXByb2ZpbGUiXX0sImRlbnRhbC1zZXJ2aWNlIjp7InJvbGVzIjpbImRvY3RvciIsInBhdGllbnQiXX19LCJzY29wZSI6InByb2ZpbGUgZW1haWwiLCJzaWQiOiJjNTA4YmEyMS0xMmM4LTQwNjAtODI4Yy03ZjljOWQ4ODFkNTUiLCJlbWFpbF92ZXJpZmllZCI6ZmFsc2UsInByZWZlcnJlZF91c2VybmFtZSI6ImRlbnRpc3RAZGVudGlzdC5kZW50aXN0IiwiZW1haWwiOiJkZW50aXN0QGRlbnRpc3QuZGVudGlzdCJ9.iNJ353yKwvrj5XAKLThq_-CewbvihX0Q9UwHZB9kAYdMlGJVMhHAkxeob82OPpzc3Xn_Xv2WFKKdQnVtCv7CQdT9IR8LCt9ctLmSfsplQUeL7fRTKIWpguisT12clMZpA1x3n8B1ZWCGJYjhmFFFgDCkD6O88x4aCw3mlcA0GzHiO9qMHnMVro7247a4F8hv_wFbNKOGFkbBSulLeLKXoBCmpR9MoVmhMn3_5PGQvKlATSpJSBGN6OCocfbKxEJdBhEzUN0yWaQosnXNWCX-FxrCHnUB8JN425qqvUu-Rr_NDhtu3AuFX6ORji74NwoBY-kHB-QsF-9PjqwFVRo-jg";

    @Test
    void shouldExtractFromToken() throws Exception {
        AuthInfoResolver authInfoResolver = new AuthInfoResolver();
        NativeWebRequest webRequest = Mockito.mock(NativeWebRequest.class);
        HttpServletRequest servletRequest = Mockito.mock(HttpServletRequest.class);
        Mockito.when(webRequest.getNativeRequest()).thenReturn(servletRequest);
        Mockito.when(servletRequest.getHeader(HttpHeaders.AUTHORIZATION))
                .thenReturn(buildToken(doctorToken))
                .thenReturn(buildToken(patientToken))
                .thenReturn(buildToken(doctorAndPatientToken))
                .thenReturn(null);

        Info doctor = authInfoResolver.resolveArgument(
                null,
                null,
                webRequest,
                null
        );
        Info patient = authInfoResolver.resolveArgument(
                null,
                null,
                webRequest,
                null
        );
        Info doctorAndPatient = authInfoResolver.resolveArgument(
                null,
                null,
                webRequest,
                null
        );

        assertThrows(
                TokenInfoExtractionException.class,
                () -> authInfoResolver.resolveArgument(
                        null,
                        null,
                        webRequest,
                        null
                )
        );

        assertEquals("dentist@dentist.dentist", doctor.getEmail());
        assertEquals(List.of(Role.DOCTOR), doctor.getRoles());

        assertEquals("patient@patient.patient", patient.getEmail());
        assertEquals(List.of(Role.PATIENT), patient.getRoles());

        assertEquals("dentist@dentist.dentist", doctorAndPatient.getEmail());
        assertEquals(List.of(Role.DOCTOR, Role.PATIENT), doctorAndPatient.getRoles());
    }

    private String buildToken(String token) {
        return "Bearer " + token;
    }
}
