import { RouterModule, Routes } from '@angular/router';
import { HomePageComponent } from '../pages/home-page/home-page.component';
import { LoginPageComponent } from '../pages/login-page/login-page.component';
import { RegistroPageComponent } from '../pages/registro-page/registro-page.component';
import { PrincipalUsuarioPageComponent } from '../pages/principal-usuario-page/principal-usuario-page.component';
import { NgModule } from '@angular/core';
import { authGuard } from './guards/auth-guard';
import { AdminSistemaPageComponent } from '../pages/admin-sistema-page/admin-sistema-page.component';
import { AdministracionUsuariosComponent } from '../components/administrador-sistema/administracion-usuarios/administracion-usuarios.component';
import { AdminsReqSistemaComponent } from '../components/administrador-sistema/admins-req-sistema/admins-req-sistema.component';


export const routes: Routes = [

    {
        path: '',
        component: HomePageComponent,


    },
    {
        path: 'login',
        component: LoginPageComponent,

    },
    {

        path: 'registro',
        component: RegistroPageComponent,

    },
    {
        path: 'menu-principal',
        component: PrincipalUsuarioPageComponent,
        canActivate: [authGuard],
    },
    {
        path: 'menu-admin-sistema',
        component: AdminSistemaPageComponent,
        canActivate: [authGuard],

        children: [

            //Van todas las URL relacionadas a la pagina de admin

            {
                path: 'usuarios',
                component: AdministracionUsuariosComponent,
            },
            {
                path: 'admins-sistema',
                component: AdminsReqSistemaComponent,
            },

            {
                path: '',
                redirectTo: 'usuarios',
                pathMatch: 'full'
            },


        ]
    },
];
