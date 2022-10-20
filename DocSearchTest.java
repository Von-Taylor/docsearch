import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Test;

public class DocSearchTest {
    @Test
    public void testHandleRequest() throws URISyntaxException, IOException {
        URI url = new URI("www.com");
        Handler handler = new Handler("");
	    assertEquals(handler.handleRequest(url), "Don't know how to handle that path!");
    }
}
