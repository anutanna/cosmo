/*
 * Copyright 2006 Open Source Applications Foundation
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.unitedinternet.cosmo.hibernate;

import java.io.Serializable;
import java.util.List;

import org.hibernate.EmptyInterceptor;
import org.hibernate.Interceptor;
import org.hibernate.type.Type;

/**
 * Hibernate Interceptor supports invoking multiple Interceptors
 */
public class CompoundInterceptor extends EmptyInterceptor {

    private static final long serialVersionUID = 1L;
    private List<Interceptor> interceptors;

    public class InterceptorParams {
        private final Object object;
        private final Serializable id;
        private final Object[] state;
        private final String[] propertyNames;
        private final Type[] types;

        public InterceptorParams(Object object, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
            this.object = object;
            this.id = id;
            this.state = state;
            this.propertyNames = propertyNames;
            this.types = types;
        }

        public Object getObject() {
            return object;
        }

        public Serializable getId() {
            return id;
        }

        public Object[] getState() {
            return state;
        }

        public String[] getPropertyNames() {
            return propertyNames;
        }

        public Type[] getTypes() {
            return types;
        }
    }

    @Override
    public boolean onFlushDirty(Object object, Serializable id, Object[] currentState, Object[] previousState,
                                String[] propertyNames, Type[] types) {
        InterceptorParams currentParams = new InterceptorParams(object, id, currentState, propertyNames, types);
        InterceptorParams previousParams = new InterceptorParams(object, id, previousState, propertyNames, types);
        return applyInterceptors(currentParams, previousParams);
    }

    @Override
    public boolean onSave(Object object, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        InterceptorParams params = new InterceptorParams(object, id, state, propertyNames, types);
        return applyInterceptors(params, null);
    }

    private boolean applyInterceptors(InterceptorParams currentParams, InterceptorParams previousParams) {
        boolean modified = false;
        for (Interceptor i : interceptors) {
            if (previousParams == null) {
                modified = modified | i.onSave(currentParams.getObject(), currentParams.getId(), currentParams.getState(),
                        currentParams.getPropertyNames(), currentParams.getTypes());
            } else {
                modified = modified | i.onFlushDirty(currentParams.getObject(), currentParams.getId(), currentParams.getState(),
                        previousParams.getState(), currentParams.getPropertyNames(),
                        currentParams.getTypes());
            }
        }
        return modified;
    }


    @Override
    public void onDelete(Object entity, Serializable id, Object[] state,
            String[] propertyNames, Type[] types) {
        for(Interceptor i: interceptors) {
            i.onDelete(entity, id, state, propertyNames, types);
        }
    }

    public void setInterceptors(List<Interceptor> interceptors) {
        this.interceptors = interceptors;
    }

}
