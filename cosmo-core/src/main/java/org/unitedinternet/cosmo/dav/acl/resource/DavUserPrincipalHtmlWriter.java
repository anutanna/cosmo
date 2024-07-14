package org.unitedinternet.cosmo.dav.acl.resource;

import org.apache.commons.text.StringEscapeUtils;
import org.apache.jackrabbit.webdav.io.OutputContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.unitedinternet.cosmo.dav.CosmoDavException;
import org.unitedinternet.cosmo.dav.DavResourceLocator;
import org.unitedinternet.cosmo.dav.WebDavResource;
import org.unitedinternet.cosmo.dav.property.WebDavProperty;
import org.unitedinternet.cosmo.model.User;
import org.unitedinternet.cosmo.util.ContentTypeUtil;
import org.unitedinternet.cosmo.util.DomWriter;
import org.w3c.dom.Element;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class DavUserPrincipalHtmlWriter {
    private static final Logger LOG = LoggerFactory.getLogger(DavUserPrincipalHtmlWriter.class);

    private User user;
    private DavResourceLocator locator;
    private DavUserPrincipalProperties properties;

    public DavUserPrincipalHtmlWriter(User user, DavResourceLocator locator, DavUserPrincipalProperties properties) {
        this.user = user;
        this.locator = locator;
        this.properties = properties;
    }

    public void writeHtmlRepresentation(OutputContext context) throws CosmoDavException, IOException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Writing html representation for user principal {}", properties.getDisplayName());
        }

        context.setContentType(ContentTypeUtil.buildContentType("text/html", "UTF-8"));
        context.setModificationTime(user.getModifiedDate());
        context.setETag("\"" + user.getEntityTag() + "\"");

        if (!context.hasStream()) {
            return;
        }

        try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(context.getOutputStream(), "utf8"))) {
            writer.write("<html>\n<head><title>");
            writer.write(StringEscapeUtils.escapeHtml4(properties.getDisplayName()));
            writer.write("</title></head>\n<body>\n<h1>");
            writer.write(StringEscapeUtils.escapeHtml4(properties.getDisplayName()));
            writer.write("</h1>\n<h2>Properties</h2>\n<dl>\n");

            for (WebDavProperty prop : properties.getProperties()) {
                Object value = prop.getValue();
                String text = value instanceof Element ? DomWriter.write((Element) value) : prop.getValueText();

                writer.write("<dt>");
                writer.write(StringEscapeUtils.escapeHtml4(prop.getName().toString()));
                writer.write("</dt><dd>");
                writer.write(StringEscapeUtils.escapeHtml4(text));
                writer.write("</dd>\n");
            }

            writer.write("</dl>\n");

            WebDavResource parent = getParent();
            writer.write("<a href=\"");
            writer.write(parent.getResourceLocator().getHref(true));
            writer.write("\">");
            writer.write(StringEscapeUtils.escapeHtml4(parent.getDisplayName()));
            writer.write("</a></li>\n");

            if (getSecurityManager() != false) {
                writer.write("<p>\n<a href=\"");
                DavResourceLocator homeLocator = locator.getFactory().createHomeLocator(locator.getContext(), user);
                writer.write(homeLocator.getHref(true));
                writer.write("\">Home collection</a><br>\n</p>\n");
            }

            writer.write("</body>\n</html>\n");
        } catch (XMLStreamException e) {
            LOG.warn("Error serializing value for property", e);
        } catch (CosmoDavException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean getSecurityManager() {
        return false;
    }

    private WebDavResource getParent() {
        // Implementation to get parent resource
        return null;
    }

}
