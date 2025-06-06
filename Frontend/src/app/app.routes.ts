import { Routes } from '@angular/router';

export const routes: Routes = [
    {
        path: '',
        pathMatch: 'full',
        redirectTo: 'home/index'
    },
    {
        path: 'home',
        children: [
            {
                path: 'index',
                pathMatch: 'full',
                loadComponent: () => import('./pages/landing-page/landing-page.component').then(m => m.LandingPageComponent)
            },
            {
                path: 'login',
                loadComponent: () => import('./pages/login/login.component').then(m => m.LoginComponent)
            },
            {
                path: 'register/:roleType',
                loadComponent: () => import('./pages/register-form/register-form.component').then(m => m.RegisterFormComponent)
            }
        ]
    }
];
