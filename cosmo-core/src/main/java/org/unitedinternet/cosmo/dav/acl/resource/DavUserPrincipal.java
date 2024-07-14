package org.unitedinternet.cosmo.dav.acl.resource;

import org.apache.jackrabbit.webdav.DavResourceIterator;
import org.apache.jackrabbit.webdav.DavResourceIteratorImpl;
import org.apache.jackrabbit.webdav.io.InputContext;
import org.apache.jackrabbit.webdav.io.OutputContext;
import org.apache.jackrabbit.webdav.property.DavPropertyName;
import org.apache.jackrabbit.webdav.property.DavPropertySet;
import org.apache.jackrabbit.webdav.version.report.ReportType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.unitedinternet.cosmo.CosmoException;
import org.unitedinternet.cosmo.dav.*;
import org.unitedinternet.cosmo.dav.acl.DavAcl;
import org.unitedinternet.cosmo.dav.acl.DavPrivilege;
import org.unitedinternet.cosmo.dav.impl.DavResourceBase;
import org.unitedinternet.cosmo.dav.property.WebDavProperty;
import org.unitedinternet.cosmo.model.User;
import org.unitedinternet.cosmo.model.UserIdentitySupplier;

import javax.xml.namespace.QName;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;

public class DavUserPrincipal extends DavResourceBase implements DavContent {
    private static final Logger LOG = LoggerFactory.getLogger(DavUserPrincipal.class);

    private User user;
    private DavUserPrincipalCollection parent;
    private DavUserPrincipalAcl acl;
    private DavUserPrincipalProperties properties;
    private DavUserPrincipalHtmlWriter htmlWriter;
    private UserIdentitySupplier userIdentitySupplier;

    public DavUserPrincipal(User user, DavResourceLocator locator, DavResourceFactory factory, UserIdentitySupplier userIdentitySupplier)
            throws CosmoDavException {
        super(locator, factory);
        this.user = user;
        this.acl = new DavUserPrincipalAcl(user);
        this.properties = new DavUserPrincipalProperties(user, locator, userIdentitySupplier);
        this.htmlWriter = new DavUserPrincipalHtmlWriter(user, locator, properties);
        this.userIdentitySupplier = userIdentitySupplier;
    }

    // Jackrabbit WebDavResource

    public String getSupportedMethods() {
        return "OPTIONS, GET, HEAD, TRACE, PROPFIND, PROPPATCH, REPORT";
    }

    public boolean isCollection() {
        return false;
    }

    public long getModificationTime() {
        return user.getModifiedDate();
    }

    public boolean exists() {
        return true;
    }

    public String getDisplayName() {
        return properties.getDisplayName();
    }

    public String getETag() {
        return "\"" + user.getEntityTag() + "\"";
    }

    public void writeTo(OutputContext context) throws CosmoDavException, IOException {
        htmlWriter.writeHtmlRepresentation(context);
    }

    public void addMember(org.apache.jackrabbit.webdav.DavResource member, InputContext inputContext)
            throws org.apache.jackrabbit.webdav.DavException {
        throw new UnsupportedOperationException();
    }

    public DavResourceIterator getMembers() {
        return new DavResourceIteratorImpl(Collections.emptyList());
    }

    public void removeMember(org.apache.jackrabbit.webdav.DavResource member) throws org.apache.jackrabbit.webdav.DavException {
        throw new UnsupportedOperationException();
    }

    public WebDavResource getCollection() {
        try {
            return getParent();
        } catch (CosmoDavException e) {
            throw new CosmoException(e);
        }
    }

    public void move(org.apache.jackrabbit.webdav.DavResource destination) throws org.apache.jackrabbit.webdav.DavException {
        throw new UnsupportedOperationException();
    }

    public void copy(org.apache.jackrabbit.webdav.DavResource destination, boolean shallow)
            throws org.apache.jackrabbit.webdav.DavException {
        throw new UnsupportedOperationException();
    }

    // WebDavResource methods

    public DavCollection getParent() throws CosmoDavException {
        if (parent == null) {
            DavResourceLocator parentLocator = getResourceLocator().getParentLocator();
            parent = (DavUserPrincipalCollection) getResourceFactory().resolve(parentLocator);
        }
        return parent;
    }

    // our methods

    public User getUser() {
        return user;
    }

    protected Set<QName> getResourceTypes() {
        return properties.getResourceTypes();
    }


    public Set<ReportType> getReportTypes() {
        return properties.getReportTypes();
    }

    protected DavAcl getAcl() {
        return acl.getAcl();
    }

    protected Set<DavPrivilege> getCurrentPrincipalPrivileges() {
        return acl.getCurrentPrincipalPrivileges(super.getCurrentPrincipalPrivileges(), user);
    }

    protected void loadLiveProperties(DavPropertySet properties) {
        this.properties.loadLiveProperties(properties);
    }

    protected void setLiveProperty(WebDavProperty property, boolean create) throws CosmoDavException {
        throw new ProtectedPropertyModificationException(property.getName());
    }

    protected void removeLiveProperty(DavPropertyName name) throws CosmoDavException {
        throw new ProtectedPropertyModificationException(name);
    }

    protected void loadDeadProperties(DavPropertySet properties) {
    }

    protected void setDeadProperty(WebDavProperty property) throws CosmoDavException {
        throw new ForbiddenException("Dead properties are not supported on this resource");
    }

    protected void removeDeadProperty(DavPropertyName name) throws CosmoDavException {
        throw new ForbiddenException("Dead properties are not supported on this resource");
    }
}
