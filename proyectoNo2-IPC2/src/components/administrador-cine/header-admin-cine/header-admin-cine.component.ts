import { Component, inject, OnInit } from '@angular/core';
import { Master } from '../../../services/masterLog/master';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { FotoPerfilSercive } from '../../../services/usuarios-service/foto-perfil.service';
import { FotoPerfilDTO } from '../../../models/usuarios/foto-perfil-dto';
import { UserLoggedDTO } from '../../../models/usuarios/user-logged-dto';

@Component({
  selector: 'app-header-admin-cine',
  imports: [RouterLink, RouterLinkActive],
  templateUrl: './header-admin-cine.component.html',
  styleUrl: './header-admin-cine.component.scss'
})
export class HeaderAdminCineComponent implements OnInit {

  loggedUsuario: string = '';

  //Atributo que sirve para redireccionar
  router = inject(Router);
  fotoPerfilUrl: string = 'icons-app/defalutUser.png';

  constructor(
    private masterSercive: Master,
    private fotoPerfilSercive: FotoPerfilSercive
  ) {

    this.readDatosLoggeados();
    this.masterSercive.onLogin.subscribe(res => {
      this.readDatosLoggeados();
    })

  }

  //Carga la foto de perfil del usuario
  ngOnInit(): void {

    const usuarioStr = localStorage.getItem("angularUserCinema");

    if (!usuarioStr) {

      this.router.navigateByUrl('/login');
      return;
    }

    const usuario = JSON.parse(usuarioStr) as UserLoggedDTO;

    this.fotoPerfilSercive.obtenerFoto(usuario.id).subscribe({
      next: (response: FotoPerfilDTO) => {

        this.fotoPerfilUrl = `data:image/jpeg;base64,${response.fotoPerfil}`;
      },
      error: (error: any) => {

        //Si hay un error que se coloque la foto default del sistema
        console.error("Error al cargar foto:", error);
        this.fotoPerfilUrl = 'icons-app/defalutUser.png';
      }
    });

  }

  //Metodo que permite verificar que el usuario este logueado
  readDatosLoggeados() {

    const datosLogged = localStorage.getItem("angularUserCinema");

    if (datosLogged != null) {

      this.loggedUsuario = datosLogged;
    }
  }

  //Metodo que permite generar el logout del administrador de cine
  onLogOutUsuario() {
    localStorage.removeItem("angularUserCinema");
    this.readDatosLoggeados();
    this.loggedUsuario = '';
    this.router.navigateByUrl("/login");

  }

}
