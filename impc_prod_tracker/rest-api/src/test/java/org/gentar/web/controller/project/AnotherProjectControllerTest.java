package org.gentar.web.controller.project;


import org.gentar.security.abac.subject.AapSystemSubject;
import org.gentar.security.abac.subject.SystemSubject;
import org.gentar.security.jwt.JwtTokenProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AnotherProjectControllerTest
{
    @Autowired
    private MockMvc mvc;

    @Autowired private JwtTokenProvider jwtTokenProvider;

    @Test
    public void shouldGenerateAuthToken() throws Exception {

        String token = "eyJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJodHRwczovL2V4cGxvcmUuYWFpLmViaS5hYy51ay9zcCIsImp0aSI6IlJZTlhpaS1uOHplZVhoNE1EcHEyNGciLCJpYXQiOjE1ODkzNTk2NDQsInN1YiI6InVzci1iMTc0ODJiZi02Y2E2LTRhMGQtOTAzMS1hMzg0Y2E1YTBiNWYiLCJlbWFpbCI6Im1tYXJ0aW5lekBlYmkuYWMudWsiLCJuaWNrbmFtZSI6Im1tYXJ0aW5lekBlYmkuYWMudWsiLCJuYW1lIjoiTWF1cmljaW8gTWFydGluZXogSmltZW5leiIsImRvbWFpbnMiOlsic2VsZi5nZW50YXItbWFpbnRhaW5lci1kb21haW4iXSwiZXhwIjoxNTg5MzYzMjQ0fQ.ZKJzfpFXPsG8Eij1yQtdKcoStiy_Im5boM6K1va7EfcD-QuO1VyPIELzlvYqjZWw-JwcaDmIf1q_g3L4hFMM3tV8R0x_PLl8948kcIwrqr3l4W3diZoSHpQOBi8heLe4kncUi1L2JdYEsGwRkIHeKkwTbacSqHlWjNKzVU3-_-XmTwnyxhK5flRP_jg9iDT-GksDfE3S3k0lz58jRfQc4PQkcr4-m_rw_aMkPRi9HyoBqX6fMzkh3-lu5mCOW_08oBYXtzGFJL-EYCrx1EqkEAMnkHQiFuXoSOD6HdVTJnYGNrTVPr8UmlOarE0C3Xz9fLzPiOgaQpYlR3VZYb4aZg";


        SystemSubject systemSubject = jwtTokenProvider.getSystemSubject(token);
        System.out.println(systemSubject);

        Authentication auth = new UsernamePasswordAuthenticationToken(systemSubject, "", null);
        SecurityContextHolder.getContext()
            .setAuthentication(auth);





        ResultActions result =
            mvc.perform(MockMvcRequestBuilders.get("/api/projects/TPN:000000005").header("Authorization", token))
               // .andExpect(status().isOk())
            ;
        String resultString = result.andReturn().getResponse().getContentAsString();
        System.out.println("------------ " + resultString);
    }
}
