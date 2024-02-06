package org.gentar.framework;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * This class allows to call REST methods, returning a string with the resource.
 * It can be used also to document the request/response when used with a ResultHandler.
 */
public class RestCaller
{
    private final MockMvc mvc;
    private final String accessToken;

    private static final String HEADER_AUTHORIZATION = "Authorization";

    public RestCaller(MockMvc mvc, String accessToken)
    {
        this.mvc = mvc;
        this.accessToken = accessToken;
    }

    public String executeGet(String url) throws Exception
    {
        ResultActions resultActions = mvc.perform(MockMvcRequestBuilders
            .get(url)
            .header(HEADER_AUTHORIZATION, "Bearer " + accessToken))
            .andExpect(status().isOk());
        MvcResult obtained = resultActions.andReturn();
        return obtained.getResponse().getContentAsString();
    }

    public String executeGetAndDocument(String url, ResultHandler documentMethod) throws Exception
    {
        ResultActions resultActions = mvc.perform(MockMvcRequestBuilders
            .get(url)
            .header(HEADER_AUTHORIZATION, "Bearer " + accessToken))
            .andExpect(status().isOk())
            .andDo(documentMethod);
        MvcResult obtained = resultActions.andReturn();
        return obtained.getResponse().getContentAsString();
    }

    public String executePost(String url, String payload) throws Exception
    {
        ResultActions resultActions = mvc.perform(MockMvcRequestBuilders
            .post(url)
            .header(HEADER_AUTHORIZATION, "Bearer " + accessToken)
            .content(payload)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
        MvcResult obtained = resultActions.andReturn();
        return obtained.getResponse().getContentAsString();
    }

    public String executePostAndDocument(String url, String payload, ResultHandler documentMethod)
    throws Exception
    {
        ResultActions resultActions = mvc.perform(MockMvcRequestBuilders
            .post(url)
            .header(HEADER_AUTHORIZATION, "Bearer " + accessToken)
            .content(payload)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(documentMethod);
        MvcResult obtained = resultActions.andReturn();
        return obtained.getResponse().getContentAsString();
    }

    public String executePostAndDocumentNoAuthentication(
        String url, String payload, ResultHandler documentMethod)
        throws Exception
    {
        ResultActions resultActions = mvc.perform(MockMvcRequestBuilders
            .post(url)
            .content(payload)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(documentMethod);
        MvcResult obtained = resultActions.andReturn();
        return obtained.getResponse().getContentAsString();
    }

    public String executePutAndDocument(String url, String payload, ResultHandler documentMethod)
        throws Exception
    {
        ResultActions resultActions = mvc.perform(MockMvcRequestBuilders
            .put(url)
            .header(HEADER_AUTHORIZATION, "Bearer " + accessToken)
            .content(payload)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(documentMethod);
        MvcResult obtained = resultActions.andReturn();
        return obtained.getResponse().getContentAsString();
    }
}
