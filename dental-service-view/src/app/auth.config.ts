import {AuthConfig} from 'angular-oauth2-oidc';

export const authConfig: AuthConfig = {
    issuer: 'http://localhost:8181/realms/dental-client',
    redirectUri: window.location.origin,
    clientId: 'dental-service',
    responseType: 'code',
    strictDiscoveryDocumentValidation: false,
    scope: 'openid profile email offline_access',
    showDebugInformation: true
}
