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
                path: 'parent-register',
                loadComponent: () => import('./pages/parent-register/parent-register.component').then(m => m.ParentRegisterComponent)
            },
            {
                path: 'child-register',
                loadComponent: () => import('./pages/child-register/child-register.component').then(m => m.ChildRegisterComponent)
            },
            {
                path: 'director-register',
                loadComponent: () => import('./pages/director-register/director-register.component').then(m => m.DirectorRegisterComponent)
            }
        ]
    }
];
