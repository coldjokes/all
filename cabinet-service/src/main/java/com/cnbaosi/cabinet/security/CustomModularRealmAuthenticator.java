package com.cnbaosi.cabinet.security;
import java.util.Collection;
import java.util.Iterator;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.pam.AuthenticationStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.realm.Realm;

/**
  * 当配置了多个Realm时，我们通常使用的认证器是shiro自带的org.apache.shiro.authc.pam.ModularRealmAuthenticator，
  * 其中决定使用的Realm的是doAuthenticate()方法
 * 
 * @author Yifeng Wang  
 */
public class CustomModularRealmAuthenticator extends ModularRealmAuthenticator {

    /**
          * 重写doMultiRealmAuthentication，抛出异常，便于捕获
     */
    @Override
    public AuthenticationInfo doMultiRealmAuthentication(Collection<Realm> realms, AuthenticationToken token) throws AuthenticationException {

        AuthenticationStrategy strategy = this.getAuthenticationStrategy();
        AuthenticationInfo aggregate = strategy.beforeAllAttempts(realms, token);

        Iterator<Realm> var5 = realms.iterator();

        while(var5.hasNext()) {
            Realm realm = (Realm)var5.next();
            aggregate = strategy.beforeAttempt(realm, token, aggregate);
            if (realm.supports(token)) {
                AuthenticationInfo info = null;
                Throwable t = null;
                info = realm.getAuthenticationInfo(token);
                aggregate = strategy.afterAttempt(realm, token, info, aggregate, t);
            }
        }
        aggregate = strategy.afterAllAttempts(token, aggregate);
        return aggregate;
    }
}

