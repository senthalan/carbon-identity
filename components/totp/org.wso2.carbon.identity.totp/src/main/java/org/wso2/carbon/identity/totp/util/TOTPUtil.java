/*
 * Copyright (c) 2015, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.identity.totp.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.context.CarbonContext;
import org.wso2.carbon.identity.application.common.IdentityApplicationManagementException;
import org.wso2.carbon.identity.application.common.model.FederatedAuthenticatorConfig;
import org.wso2.carbon.identity.application.common.model.IdentityProvider;
import org.wso2.carbon.identity.application.common.model.Property;
import org.wso2.carbon.identity.application.common.util.IdentityApplicationConstants;
import org.wso2.carbon.identity.application.common.util.IdentityApplicationManagementUtil;
import org.wso2.carbon.identity.totp.Constants;
import org.wso2.carbon.idp.mgt.IdentityProviderManagementException;
import org.wso2.carbon.idp.mgt.IdentityProviderManager;

/**
 * TOTP Util class.
 */
public class TOTPUtil {
	private static Log log = LogFactory.getLog(TOTPUtil.class);

	/**
	 * Get locally stored encoding method.
	 *
	 * @return
	 * @throws IdentityApplicationManagementException
	 */
	public static String getEncodingMethod() throws IdentityApplicationManagementException,
            IdentityProviderManagementException {

		String tenantDomain = CarbonContext.getThreadLocalCarbonContext().getTenantDomain();
		IdentityProviderManager identityProviderManager = IdentityProviderManager.getInstance();

		IdentityProvider identityProvider = identityProviderManager.getResidentIdP(tenantDomain);
		FederatedAuthenticatorConfig federatedAuthenticatorConfig = IdentityApplicationManagementUtil.
                getFederatedAuthenticator(identityProvider.getFederatedAuthenticatorConfigs(),
                        IdentityApplicationConstants.Authenticator.TOTP.NAME);
		Property property = IdentityApplicationManagementUtil.
				getProperty(federatedAuthenticatorConfig.getProperties(),
                        IdentityApplicationConstants.Authenticator.TOTP.ENCODING_METHOD);
		if (log.isDebugEnabled()) {
			log.debug("Read the encoding method from Resident Idp for tenant id : " + CarbonContext.
                    getThreadLocalCarbonContext().getTenantId());
		}

		if (Constants.BASE32.equals(property.getValue())) {
			return Constants.BASE32;
		}
		return Constants.BASE64;
	}
}