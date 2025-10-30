import { Component, inject, OnInit } from '@angular/core';
import { UsuarioDatosDTO } from '../../../models/usuarios/usuario-datos-dto';
import { Router } from '@angular/router';
import { UserLoggedDTO } from '../../../models/usuarios/user-logged-dto';
import { UsuarioService } from '../../../services/usuarios-service/usuario.service';

@Component({
  selector: 'app-ver-perfil',
  imports: [],
  templateUrl: './ver-perfil.component.html',
  styleUrl: './ver-perfil.component.scss'
})
export class VerPerfilComponent implements OnInit {

  usuarioDatos: UsuarioDatosDTO = {
    id: '',
    correo: '',
    nombre: '',
    foto: '',
    telefono: '',
    pais: '',
    identificacion: '',
    rol: ''
  };

  constructor(
    private usuarioService: UsuarioService
  ) {

  }

  router = inject(Router);

  //Metodo que permite cargar los datos del usuario al inicializar el componente
  ngOnInit(): void {
    const usuarioStr = localStorage.getItem("angularUserCinema");

    if (!usuarioStr) {

      this.router.navigateByUrl('/login');
      return;
    }

    const usuario = JSON.parse(usuarioStr) as UserLoggedDTO;

    this.usuarioService.consultarPerfil(usuario.id).subscribe({
      next: (response: UsuarioDatosDTO) => {

        this.usuarioDatos = response;
      },
      error: (error: any) => {
        console.error("Error al cargar foto:", error);
      }
    });
  }


  //Metodo que sirve para poder generar la foto de perfil obtenida de backend
  getImagenBase64(base64: string): string {
    if (base64.startsWith('data:image')) {
      return base64;
    }
    return `data:image/jpeg;base64,${base64}`;
  }


  getTipoUsuario(tipo: string): string {

    if (!tipo) {
      return 'NO REGISTRADO';
    }


    if (tipo === 'ADMINISTRADOR_SISTEMA') {
      return 'ADMINISTRADOR DE SISTEMA';
    }
    else if (tipo === 'ADMINISTRADOR_CINE') {
      return 'ADMINISTRADOR DE CINE';
    }
    else if(tipo === 'USUARIO_ESPECIAL'){
      return 'ANUNCIANTE';
    }
    else{
      return 'USUARIO';
    }

  }

}
