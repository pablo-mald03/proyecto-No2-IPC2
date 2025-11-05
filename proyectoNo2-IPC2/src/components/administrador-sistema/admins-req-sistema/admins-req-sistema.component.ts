import { Component, inject, OnInit } from '@angular/core';
import { AdminsSistemaCardComponent } from "../admins-sistema-card/admins-sistema-card.component";
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { Usuario } from '../../../models/usuarios/usuario';
import { UsuarioDatosDTO } from '../../../models/usuarios/usuario-datos-dto';
import { UsuariosSistemaService } from '../../../services/admin-sistema-service/usuarios-sistema.service';
import { CantidadRegistrosDTO } from '../../../models/usuarios/cantidad-registros-dto';

@Component({
  selector: 'app-admins-req-sistema',
  imports: [AdminsSistemaCardComponent, RouterLink, RouterLinkActive],
  templateUrl: './admins-req-sistema.component.html',
  styleUrl: './admins-req-sistema.component.scss'
})
export class AdminsReqSistemaComponent implements OnInit {

  administradores: UsuarioDatosDTO[] = [];
  administradoresMostrados: UsuarioDatosDTO[] = [];


  //Apartado de atributos que sirven para cargar dinamicamente los atributos
  indiceActual = 0;
  cantidadPorCarga = 2;
  totalReportes = 0;
  todosCargados = false;

  router = inject(Router);

  constructor(

    private usuariosSistemaService: UsuariosSistemaService,


  ) { }

  //Metodo que carga la cantidad de registros al iniciar la pagina 
  ngOnInit(): void {

    this.indiceActual = 0;
    this.administradoresMostrados = [];
    this.todosCargados = true;

    this.usuariosSistemaService.cantidadRegistros().subscribe({
      next: (cantidadDTO: CantidadRegistrosDTO) => {

        this.totalReportes = cantidadDTO.cantidad;
        this.indiceActual = 0;
        this.administradoresMostrados = [];
        this.todosCargados = false;

        this.cargarMasUsuarios();
      },
      error: (error: any) => {

        //this.mostrarError(error);

      }
    });
  }

  //Metodo que ayuda a volver
  volver() {
    this.router.navigateByUrl('/menu-admin-sistema/usuarios')

  }

  //Metodo que va cargando dinamicamente los usuarios
  cargarMasUsuarios(): void {

    if (this.todosCargados) return;

    this.usuariosSistemaService.listadoRegistros(this.cantidadPorCarga, this.indiceActual).subscribe({
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
