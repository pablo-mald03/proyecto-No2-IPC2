import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UserLoggedDTO } from '../../../models/usuarios/user-logged-dto';
import { CineDTO } from '../../../models/cines/cine-dto';
import { CinesAsociadosService } from '../../../services/cine-service/cines-asociados.sercive';
import { CantidadRegistrosDTO } from '../../../models/usuarios/cantidad-registros-dto';
import { CinesAdminCineCardsComponent } from "../cines-admin-cine-cards/cines-admin-cine-cards.component";

@Component({
  selector: 'app-gestion-cines-admin-cine',
  imports: [CinesAdminCineCardsComponent],
  templateUrl: './gestion-cines-admin-cine.component.html',
  styleUrl: './gestion-cines-admin-cine.component.scss'
})
export class GestionCinesAdminCineComponent implements OnInit {

  idUsuario!: string;


  //Arreglos que se van a llenar en base a los cines que tiene asignado el usuario
  cinesMostrados: CineDTO[] = [];

  //Apartado de atributos que sirven para cargar dinamicamente los atributos
  indiceActual = 0;
  cantidadPorCarga = 2;
  totalReportes = 0;
  todosCargados = false;


  constructor(
    private router: Router,
    private cinesAsociadosService: CinesAsociadosService,
  ) {

  }

  //metodo utilizado para poder cargar los datos del servicio
  ngOnInit(): void {

    const usuarioStr = localStorage.getItem("angularUserCinema");

    if (!usuarioStr) {

      this.router.createUrlTree(['/acceso-denegado']);
      return;
    }

    const usuario = JSON.parse(usuarioStr) as UserLoggedDTO;

    this.idUsuario = usuario.id;

    this.indiceActual = 0;
    this.cinesMostrados = [];
    this.todosCargados = true;


    this.cargarCinesRegistrados();


  }

  //Metodo que permite llamar al metodo que carga dinamicamente los registros
  mostrarMasAnuncios(): void {

    if (this.todosCargados || this.cinesMostrados.length === 0) {
      return;
    }

    this.cargarMasRegistros();
  }

  //Metodo que permite cargar los anuncios registrados
  cargarCinesRegistrados() {

    this.cinesAsociadosService.cantidadRegistros(this.idUsuario).subscribe({
      next: (cantidadDTO: CantidadRegistrosDTO) => {
        this.totalReportes = cantidadDTO.cantidad;
        this.indiceActual = 0;
        this.cinesMostrados = [];
        this.todosCargados = false;

        this.cargarMasRegistros();
      },
      error: (error: any) => {

        console.log(error);

      }
    });

  }


   //Carga dinamicamente la cantidad establecida de anuncios para no saturar la web
  cargarMasRegistros(): void {

    this.cinesAsociadosService.listadoRegistros(this.cantidadPorCarga, this.indiceActual, this.idUsuario).subscribe({
      next: (response: CineDTO[]) => {

        if (!response || response.length === 0) {
          this.todosCargados = true;
          return;
        }

        this.cinesMostrados.push(...response);
        this.indiceActual += response.length;

        if (this.indiceActual >= this.totalReportes) {
          this.todosCargados = true;
        }


      },
      error: (error: any) => {

        console.log(error);

      }
    });
  }
}
