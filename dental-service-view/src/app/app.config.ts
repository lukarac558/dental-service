import { AuthConfig } from 'angular-oauth2-oidc';

export const appConfig = {
    apiUrl: 'https://localhost:8080/api',
    authSecurity: 'https://auth-service:8443/realms/dental-client/account/#/personal-info'
}

export const authConfig: AuthConfig = {
    //To be used if backend is dockerized
    issuer: 'https://auth-service:8443/realms/dental-client',
    //To be used if backend is launched locally
    //issuer: 'http://localhost:8181/realms/dental-client',
    redirectUri: window.location.origin,
    clientId: 'dental-service',
    responseType: 'code',
    strictDiscoveryDocumentValidation: false,
    scope: 'openid profile email offline_access',
    showDebugInformation: true,
    requireHttps: false
}
