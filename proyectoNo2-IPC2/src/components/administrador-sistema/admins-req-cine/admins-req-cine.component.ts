import { Component, inject, OnInit } from '@angular/core';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { AdminsCineCardsComponent } from "../admins-cine-cards/admins-cine-cards.component";
import { Usuario } from '../../../models/usuarios/usuario';
import { UsuarioDatosDTO } from '../../../models/usuarios/usuario-datos-dto';
import { UsuarioAdminCineService } from '../../../services/admin-sistema-service/admin-cine.service';
import { CantidadRegistrosDTO } from '../../../models/usuarios/cantidad-registros-dto';

@Component({
  selector: 'app-admins-req-cine',
  imports: [RouterLink, RouterLinkActive, AdminsCineCardsComponent, AdminsCineCardsComponent],
  templateUrl: './admins-req-cine.component.html',
  styleUrl: './admins-req-cine.component.scss'
})
export class AdminsReqCineComponent implements OnInit {

  administradores: UsuarioDatosDTO[] = [];
  administradoresMostrados: UsuarioDatosDTO[] = [];




  //Apartado de atributos que sirven para cargar dinamicamente los atributos
  indiceActual = 0;
  cantidadPorCarga = 2;
  totalReportes = 0;
  todosCargados = false;

  router = inject(Router);

  constructor(

    private usuariosCineService: UsuarioAdminCineService,


  ) { }


  ngOnInit(): void {

    this.indiceActual = 0;
    this.administradoresMostrados = [];
    this.todosCargados = true;

    this.usuariosCineService.cantidadRegistros().subscribe({
      next: (cantidadDTO: CantidadRegistrosDTO) => {

        this.totalReportes = cantidadDTO.cantidad;

        this.indiceActual = 0;
        this.administradoresMostrados = [];
        this.todosCargados = false;

        this.cargarMasAdministradores();
      },
      error: (error: any) => {

        console.log(error);

      }
    });
  }


  //Metodo que sirve para cargar dinamicamente a los administradores 
  cargarMasAdministradores(): void {
    if (this.todosCargados) return;

    this.usuariosCineService.listadoRegistros(this.cantidadPorCarga, this.indiceActual).subscribe({
      next: (response: UsuarioDatosDTO[]) => {
        this.ampliarResultados(response);

      },
      error: (error: any) => {

        //this.mostrarError(error);

      }
    });
  }

  //Metodo encargado de ir ampliando dinamicamente los usuarios registrados
  ampliarResultados(response: UsuarioDatosDTO[]): void {

    if (!response || response.length === 0) {
      this.todosCargados = true;
      return;
    }

    this.administradoresMostrados.push(...response);
    this.indiceActual += response.length;

    if (this.indiceActual >= this.totalReportes) {
      this.todosCargados = true;
    }

  }


}
