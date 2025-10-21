import { Component, inject, OnInit } from '@angular/core';
import { UserLoggedDTO } from '../../../models/usuarios/user-logged-dto';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';

@Component({
  selector: 'app-dashboard-bienvenida-anunciante',
  imports: [RouterLink, RouterLinkActive],
  templateUrl: './dashboard-bienvenida-anunciante.component.html',
  styleUrl: './dashboard-bienvenida-anunciante.component.scss'
})
export class DashboardBienvenidaAnuncianteComponent implements OnInit {

  idUsuario!: string;

  router = inject(Router);

  ngOnInit(): void {

    const usuarioStr = localStorage.getItem("angularUserCinema");

    if (!usuarioStr) {

      this.router.createUrlTree(['/acceso-denegado']);
      return;
    }

    const usuario = JSON.parse(usuarioStr) as UserLoggedDTO;

    this.idUsuario = usuario.id;

    

  }




}
