import { RouterModule, Routes } from '@angular/router';
import { HomePageComponent } from '../pages/home-page/home-page.component';
import { LoginPageComponent } from '../pages/login-page/login-page.component';
import { RegistroPageComponent } from '../pages/registro-page/registro-page.component';
import { PrincipalUsuarioPageComponent } from '../pages/principal-usuario-page/principal-usuario-page.component';
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
import { AdminCinePageComponent } from '../pages/admin-cine-page/admin-cine-page.component';
import { ReporteComentariosSalasComentadasComponent } from '../components/reportes-cine/reporte-comentarios-salas-comentadas/reporte-comentarios-salas-comentadas.component';
import { ReportePeliculasProyectadasComponent } from '../components/reportes-cine/reporte-peliculas-proyectadas/reporte-peliculas-proyectadas.component';
import { ReporteSalasGustadasCineComponent } from '../components/reportes-cine/reporte-salas-gustadas-cine/reporte-salas-gustadas-cine.component';
import { ReporteBoletosVendidosComponent } from '../components/reportes-cine/reporte-boletos-vendidos/reporte-boletos-vendidos.component';
import { AnunciantePageComponent } from '../pages/anunciante-page.component/anunciante-page.component';
import { DashboardBienvenidaAnuncianteComponent } from '../components/pagina-anunciante/dashboard-bienvenida-anunciante/dashboard-bienvenida-anunciante.component';
import { rolAnuncianteGuard } from './guards/rol-anunciante-guard';
import { AccesoDenegadoPageComponent } from '../pages/acceso-denegado-page/acceso-denegado-page.component';
import { AnunciosCompradosComponent } from '../components/pagina-anunciante/anuncios-comprados/anuncios-comprados.component';
import { ComprarAnuncioComponent } from '../components/pagina-anunciante/comprar-anuncio.component/comprar-anuncio.component';


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
        path: 'acceso-denegado',
        component: AccesoDenegadoPageComponent,
        canActivate: [authGuard],

    },
    {
        path: 'menu-principal',
        component: PrincipalUsuarioPageComponent,
        canActivate: [authGuard],
    },
    {
        path: 'menu-anunciante',
        component: AnunciantePageComponent,
        canActivate: [authGuard,rolAnuncianteGuard],

        //Todas las paginas y funcionalidades que tiene el anunciante
        children: [
            {
                path: 'inicio',
                component: DashboardBienvenidaAnuncianteComponent,
            },
            {
                path: 'anuncios/comprados/:id',
                component: AnunciosCompradosComponent,
            },
            {
                path: 'anuncios/compra',
                component: ComprarAnuncioComponent,
            },
            {
                path: '',
                redirectTo: 'inicio',
                pathMatch: 'full'
            },

        ]
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
                path: 'reportes/anuncios',
                component: ReporteAnunciosComponent,
            },
            {
                path: 'reportes/anunciantes',
                component: ReporteGananciasAnuncianteComponent,
            },
            {
                path: 'reportes/salas-gustadas',
                component: ReporteSalasGustadasComponent,
            },
            {
                path: 'reportes/salas-comentadas',
                component: ReporteSalasComentadasComponent,
            },
            {
                path: 'reportes/ganancias',
                component: ReporteGananciasComponent,
            },
            {
                path: '',
                redirectTo: 'usuarios',
                pathMatch: 'full'
            },


        ]
    },
    {
        path: 'menu-admin-cine',
        component: AdminCinePageComponent,
        canActivate: [authGuard],

        //Directorios de funcionalidades dentro de la pagina de administradores de cine
        children: [
            {
                path: 'reportes/salas/comentadas',
                component: ReporteComentariosSalasComentadasComponent,
            },
            {
                path: 'reportes/peliculas',
                component: ReportePeliculasProyectadasComponent,
            },
            {
                path: 'reportes/salas/gustadas',
                component: ReporteSalasGustadasCineComponent,
            },
            {
                path: 'reportes/boletos',
                component: ReporteBoletosVendidosComponent,
            },

        ]
    },
];
