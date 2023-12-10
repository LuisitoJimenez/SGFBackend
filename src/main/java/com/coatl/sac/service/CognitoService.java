package com.coatl.sac.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.amazonaws.services.cognitoidp.model.AdminCreateUserResult;
import com.amazonaws.services.cognitoidp.model.AdminDisableUserRequest;
import com.amazonaws.services.cognitoidp.model.AdminDisableUserResult;
import com.amazonaws.services.cognitoidp.model.AdminUpdateUserAttributesRequest;
import com.amazonaws.services.cognitoidp.model.AdminUpdateUserAttributesResult;
import com.amazonaws.services.cognitoidp.model.AdminUserGlobalSignOutRequest;
import com.amazonaws.services.cognitoidp.model.AdminUserGlobalSignOutResult;
import com.amazonaws.services.cognitoidp.model.AdminCreateUserRequest;
import com.amazonaws.services.cognitoidp.model.AttributeType;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClient;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClientBuilder;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CognitoService {

    private final Environment environment;
    private final AWSCognitoIdentityProvider cognitoIdentityProvider;

    public AdminCreateUserResult createUser(String userName, String password, String first, String last,
            String secondLast) {

        AdminCreateUserRequest createUserRequest = new AdminCreateUserRequest()
                .withUserPoolId(environment.getProperty("aws.cognito.pool.userPoolId"))
                .withUsername(userName)
                .withTemporaryPassword(password)
                .withUserAttributes(
                        new AttributeType().withName("email").withValue(userName),
                        new AttributeType().withName("name").withValue(first),
                        new AttributeType().withName("custom:last").withValue(last),
                        new AttributeType().withName("custom:second_last").withValue(secondLast));

        AdminCreateUserResult createUserResult = cognitoIdentityProvider.adminCreateUser(createUserRequest);

        return createUserResult;

    }

    public AdminUpdateUserAttributesResult updateCognitoUserAttributes(String cognitoUserName, String email,
            String first, String last,
            String secondLast) {
        AWSCognitoIdentityProvider cognitoIdentityProvider = AWSCognitoIdentityProviderClientBuilder.defaultClient();

        List<AttributeType> userAttributes = new ArrayList<>();

        userAttributes.add(new AttributeType().withName("email").withValue(email));
        userAttributes.add(new AttributeType().withName("name").withValue(first));
        userAttributes.add(new AttributeType().withName("custom:last").withValue(last));
        userAttributes.add(new AttributeType().withName("custom:second_last").withValue(secondLast));

        AdminUpdateUserAttributesRequest updateUserAttributesRequest = new AdminUpdateUserAttributesRequest()
                .withUserPoolId(environment.getProperty("aws.cognito.pool.userPoolId"))
                .withUsername(cognitoUserName)
                .withUserAttributes(userAttributes);

        AdminUpdateUserAttributesResult updateUserAttributesResult = cognitoIdentityProvider
                .adminUpdateUserAttributes(updateUserAttributesRequest);

        return updateUserAttributesResult;

    }

    public AdminDisableUserResult adminDisableCognitoUser(String cognitoUsername) {
        AWSCognitoIdentityProvider cognitoIdentityProvider = AWSCognitoIdentityProviderClient.builder().build();

        AdminDisableUserRequest disableUserRequest = new AdminDisableUserRequest()
                .withUserPoolId(environment.getProperty("aws.cognito.pool.userPoolId"))
                .withUsername(cognitoUsername);

        AdminDisableUserResult disableUserResult = cognitoIdentityProvider.adminDisableUser(disableUserRequest);

        return disableUserResult;

    }

    public AdminUserGlobalSignOutResult adminSignOutCognitoUser(String cognitoUsername) {
        AWSCognitoIdentityProvider cognitoIdentityProvider = AWSCognitoIdentityProviderClient.builder().build();

        AdminUserGlobalSignOutRequest signOutRequest = new AdminUserGlobalSignOutRequest()
                .withUserPoolId(environment.getProperty("aws.cognito.pool.userPoolId"))
                .withUsername(cognitoUsername);

        AdminUserGlobalSignOutResult signOutResult = cognitoIdentityProvider.adminUserGlobalSignOut(signOutRequest);

        return signOutResult;

    }

}
