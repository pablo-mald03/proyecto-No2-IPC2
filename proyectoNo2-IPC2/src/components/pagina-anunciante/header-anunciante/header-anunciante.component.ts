import { Component, inject, OnInit } from '@angular/core';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { Master } from '../../../services/masterLog/master';
import { FotoPerfilSercive } from '../../../services/usuarios-service/foto-perfil.service';
import { UserLoggedDTO } from '../../../models/usuarios/user-logged-dto';
import { FotoPerfilDTO } from '../../../models/usuarios/foto-perfil-dto';

@Component({
  selector: 'app-header-anunciante',
  imports: [RouterLinkActive, RouterLink],
  templateUrl: './header-anunciante.component.html',
  styleUrl: './header-anunciante.component.scss'
})
export class HeaderAnuncianteComponent implements OnInit{

  loggedUsuario: string = '';

  router = inject(Router);

  fotoPerfilUrl: string = 'icons-app/defalutUser.png';

  constructor(
    private masterSercive: Master,
    private fotoPerfilSercive: FotoPerfilSercive) {

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
