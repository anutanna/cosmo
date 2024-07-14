package org.unitedinternet.cosmo.dav.acl.resource;

import org.apache.jackrabbit.webdav.property.DavPropertySet;
import org.apache.jackrabbit.webdav.version.report.ReportType;
import org.unitedinternet.cosmo.dav.DavResourceLocator;
import org.unitedinternet.cosmo.dav.acl.property.AlternateUriSet;
import org.unitedinternet.cosmo.dav.acl.property.GroupMembership;
import org.unitedinternet.cosmo.dav.acl.property.PrincipalUrl;
import org.unitedinternet.cosmo.dav.acl.report.PrincipalMatchReport;
import org.unitedinternet.cosmo.dav.caldav.property.CalendarHomeSet;
import org.unitedinternet.cosmo.dav.caldav.property.CalendarUserAddressSet;
import org.unitedinternet.cosmo.dav.caldav.property.ScheduleInboxURL;
import org.unitedinternet.cosmo.dav.caldav.property.ScheduleOutboxURL;
import org.unitedinternet.cosmo.dav.property.*;
import org.unitedinternet.cosmo.model.User;
import org.unitedinternet.cosmo.model.UserIdentity;
import org.unitedinternet.cosmo.model.UserIdentitySupplier;

import javax.xml.namespace.QName;
import java.util.HashSet;
import java.util.Set;

import static org.unitedinternet.cosmo.dav.acl.AclConstants.RESOURCE_TYPE_PRINCIPAL;

public class DavUserPrincipalProperties {
    private User user;
    private DavResourceLocator locator;
    private UserIdentitySupplier userIdentitySupplier;

    private static final Set<ReportType> REPORT_TYPES = new HashSet<>();

    static {
        REPORT_TYPES.add(PrincipalMatchReport.REPORT_TYPE_PRINCIPAL_MATCH);
    }

    public DavUserPrincipalProperties(User user, DavResourceLocator locator, UserIdentitySupplier userIdentitySupplier) {
        this.user = user;
        this.locator = locator;
        this.userIdentitySupplier = userIdentitySupplier;
    }


    public String getDisplayName() {
        UserIdentity userIdentity = userIdentitySupplier.forUser(user);
        String firstName = userIdentity.getFirstName();
        String lastName = userIdentity.getLastName();
        String email = userIdentity.getEmails().isEmpty() ? "" : userIdentity.getEmails().iterator().next();

        if (firstName == null && lastName == null) {
            return email;
        } else if (firstName == null) {
            return lastName;
        } else if (lastName == null) {
            return firstName;
        } else {
            return firstName + " " + lastName;
        }
    }

    public Set<QName> getResourceTypes() {
        HashSet<QName> rt = new HashSet<>(1);
        rt.add(RESOURCE_TYPE_PRINCIPAL);
        return rt;
    }

    public Set<ReportType> getReportTypes() {
        return REPORT_TYPES;
    }

    public void loadLiveProperties(DavPropertySet properties) {
        properties.add(new CreationDate(user.getCreationDate()));
        properties.add(new DisplayName(getDisplayName()));
        properties.add(new ResourceType(getResourceTypes()));
        properties.add(new IsCollection(false));
        properties.add(new Etag(user.getEntityTag()));
        properties.add(new LastModified(user.getModifiedDate()));
        properties.add(new CalendarHomeSet(locator, user));

        if (isSchedulingEnabled()) {
            properties.add(new CalendarUserAddressSet(user, userIdentitySupplier));
            properties.add(new ScheduleInboxURL(locator, user));
            properties.add(new ScheduleOutboxURL(locator, user));
        }

        properties.add(new AlternateUriSet());
        properties.add(new PrincipalUrl(locator, user));
        properties.add(new GroupMembership());
    }

    private boolean isSchedulingEnabled() {
        // Implementation of the scheduling enabled check
        return true;
    }

    public WebDavProperty[] getProperties() {
        WebDavProperty[] webDavProperties = new WebDavProperty[0];
        return webDavProperties;
    }
}
