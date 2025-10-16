import { Routes } from '@angular/router';
import { HomePageComponent } from '../pages/home-page/home-page.component';
import { LoginPageComponent } from '../pages/login-page/login-page.component';
import { RegistroPageComponent } from '../pages/registro-page/registro-page.component';
import { PrincipalUsuarioPageComponent } from '../pages/principal-usuario-page/principal-usuario-page.component';


export const routes: Routes = [

    {
        path: '',
        //component: HomePageComponent,
        component: PrincipalUsuarioPageComponent,

    },
    {
        path: 'login',
        component: LoginPageComponent,

    },
    {

        path: 'registro',
        component: RegistroPageComponent,

    },

];
