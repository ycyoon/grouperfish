package com.mozilla.grouperfish.rest.jaxrs;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

import org.testng.annotations.Test;

import com.mozilla.grouperfish.naming.Scope;
import com.mozilla.grouperfish.rest.jaxrs.DocumentsResource;
import com.mozilla.grouperfish.rest.jaxrs.RestHelper;
import com.mozilla.grouperfish.rest.jaxrs.ConfigurationsResource.TransformConfigsResource;
import com.mozilla.grouperfish.services.api.Grid;
import com.mozilla.grouperfish.services.mock.MockGrid;


@Test(groups="unit")
public class RestHelperTest {

    private final Grid grid = new MockGrid();
    private final Scope NS = new Scope("unit-test", grid);

    public void testPutAny() throws IOException {
        final HttpServletRequest mock = mock(HttpServletRequest.class);

        final String body = "{\"id\": \"mydoc\"}";
        when(mock.getMethod()).thenReturn("PUT");
        when(mock.getContentLength()).thenReturn(body.length());
        when(mock.getInputStream()).thenReturn(new ServletInputStream() {
            final ByteArrayInputStream byteStream = new ByteArrayInputStream(body.getBytes());
            @Override
            public int read() throws IOException {
                return byteStream.read();
            }
        });

        final Response response = RestHelper.putAny(DocumentsResource.class, NS, "mydoc", mock);
        assertNotNull(response);
        assertEquals(201, response.getStatus());
    }


    public void testDeleteAny() throws IOException {
        final HttpServletRequest mock = mock(HttpServletRequest.class);
        when(mock.getMethod()).thenReturn("DELETE");

        final Response response = RestHelper.deleteAny(DocumentsResource.class, NS, "somedoc", mock);
        assertNotNull(response);
        assertEquals(204, response.getStatus());
    }


    public void testGetAny() {
        // Put stuff in, to get afterwards:
        NS.documents().put("myGetDoc", "{\"id\": \"myGetDoc\"}");

        final HttpServletRequest mock = mock(HttpServletRequest.class);
        final Response response = RestHelper.getAny(DocumentsResource.class, NS, "myGetDoc", mock);
        assertNotNull(response);
        assertEquals(200, response.getStatus());

        final Response response404 = RestHelper.getAny(DocumentsResource.class, NS, "no such doc", mock);
        assertNotNull(response404);
        assertEquals(404, response404.getStatus());
    }


    public void testListAny() {
        final HttpServletRequest mock = mock(HttpServletRequest.class);
        final Response response = RestHelper.listAny(TransformConfigsResource.class, NS, mock);
        assertNotNull(response);
        assertEquals(200, response.getStatus());
    }

}
