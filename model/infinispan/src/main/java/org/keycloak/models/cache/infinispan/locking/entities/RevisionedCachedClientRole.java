package org.keycloak.models.cache.infinispan.locking.entities;

import org.keycloak.models.RealmModel;
import org.keycloak.models.RoleModel;
import org.keycloak.models.cache.entities.CachedClientRole;
import org.keycloak.models.cache.infinispan.locking.Revisioned;

/**
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision: 1 $
 */
public class RevisionedCachedClientRole extends CachedClientRole implements Revisioned {

    public RevisionedCachedClientRole(Long revision, String idClient, RoleModel model, RealmModel realm) {
        super(idClient, model, realm);
        this.revision = revision;
    }

    private Long revision;

    @Override
    public Long getRevision() {
        return revision;
    }

    @Override
    public void setRevision(Long revision) {
        this.revision = revision;
    }


}
