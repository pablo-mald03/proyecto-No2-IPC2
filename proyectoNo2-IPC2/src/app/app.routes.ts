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
import { CrearAdminSistemaComponent } from '../components/administrador-sistema/crear-admin-sistema/crear-admin-sistema.component';
import { CrearAdminCineComponent } from '../components/administrador-sistema/crear-admin-cine/crear-admin-cine.component';
import { AdminsReqCineComponent } from '../components/administrador-sistema/admins-req-cine/admins-req-cine.component';
import { ReporteAnunciosComponent } from '../components/reportes-sistema/reporte-anuncios/reporte-anuncios.component';
import { ReporteGananciasAnuncianteComponent } from '../components/reportes-sistema/reporte-ganancias-anunciante/reporte-ganancias-anunciante.component';
import { ReporteSalasGustadasComponent } from '../components/reportes-sistema/reporte-salas-gustadas/reporte-salas-gustadas.component';
import { ReporteSalasComentadasComponent } from '../components/reportes-sistema/reporte-salas-comentadas/reporte-salas-comentadas.component';
import { ReporteGananciasComponent } from '../components/reportes-sistema/reporte-ganancias/reporte-ganancias.component';


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
                path: 'crear-admin-sistema',
                component: CrearAdminSistemaComponent,
            },
            {
                path: 'admins-cine',
                component: AdminsReqCineComponent,
            },
            {
                path: 'crear-admin-cine',
                component: CrearAdminCineComponent,
            },
            {
                path: 'reporte-anuncios',
                component: ReporteAnunciosComponent,
            },
            {
                path: 'reporte-anunciantes',
                component: ReporteGananciasAnuncianteComponent,
            },
            {
                path: 'reporte-salas-gustadas',
                component: ReporteSalasGustadasComponent,
            },
            {
                path: 'reporte-salas-comentadas',
                component: ReporteSalasComentadasComponent,
            },
            {
                path: 'reporte-ganancias',
                component: ReporteGananciasComponent,
            },
            {
                path: '',
                redirectTo: 'usuarios',
                pathMatch: 'full'
            },


        ]
    },
];
