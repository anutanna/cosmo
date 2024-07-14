package org.unitedinternet.cosmo.dav.acl.resource;

import org.unitedinternet.cosmo.dav.acl.DavAce;
import org.unitedinternet.cosmo.dav.acl.DavAcl;
import org.unitedinternet.cosmo.dav.acl.DavPrivilege;
import org.unitedinternet.cosmo.model.User;
import org.unitedinternet.cosmo.security.impl.CosmoSecurityManagerImpl;

import java.util.Set;

public class DavUserPrincipalAcl {
    private DavAcl acl;

    public DavUserPrincipalAcl(User user) {
        this.acl = makeAcl(user);
    }

    private DavAcl makeAcl(User user) {
        DavAcl acl = new DavAcl();

        DavAce unauthenticated = new DavAce.UnauthenticatedAce();
        unauthenticated.setDenied(true);
        unauthenticated.getPrivileges().add(DavPrivilege.ALL);
        unauthenticated.setProtected(true);
        acl.getAces().add(unauthenticated);

        DavAce owner = new DavAce.SelfAce();
        owner.getPrivileges().add(DavPrivilege.ALL);
        owner.setProtected(true);
        acl.getAces().add(owner);

        DavAce allAllow = new DavAce.AllAce();
        allAllow.getPrivileges().add(DavPrivilege.READ_CURRENT_USER_PRIVILEGE_SET);
        allAllow.setProtected(true);
        acl.getAces().add(allAllow);

        DavAce allDeny = new DavAce.AllAce();
        allDeny.setDenied(true);
        allDeny.getPrivileges().add(DavPrivilege.ALL);
        allDeny.setProtected(true);
        acl.getAces().add(allDeny);

        return acl;
    }

    public DavAcl getAcl() {
        return acl;
    }

    public Set<DavPrivilege> getCurrentPrincipalPrivileges(Set<DavPrivilege> privileges, User user) {
        if (!privileges.isEmpty()) {
            return privileges;
        }

        CosmoSecurityManagerImpl securityManager = null;
        SecurityContext context = (SecurityContext) securityManager.getSecurityContext();
        User currentUser = context.getUser();
        if (currentUser != null && currentUser.equals(user)) {
            privileges.add(DavPrivilege.READ);
        }

        return privileges;
    }

    private class SecurityContext {
        public User getUser() {
            return null;
        }
    }
}
