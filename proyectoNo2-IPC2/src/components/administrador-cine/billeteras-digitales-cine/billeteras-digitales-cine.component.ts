import { Component } from '@angular/core';
import { BilleteraCineDTO } from '../../../models/admins-cine/billetera-cine-dto';
import { Router } from '@angular/router';
import { BilleteraCineService } from '../../../services/admin-cine-service/billetera-cine.service';
import { UserLoggedDTO } from '../../../models/usuarios/user-logged-dto';
import { CantidadRegistrosDTO } from '../../../models/usuarios/cantidad-registros-dto';
import { BilleteraDigitalCardComponent } from "../billetera-digital-card/billetera-digital-card.component";

@Component({
  selector: 'app-billeteras-digitales-cine',
  imports: [ BilleteraDigitalCardComponent],
  templateUrl: './billeteras-digitales-cine.component.html',
  styleUrl: './billeteras-digitales-cine.component.scss'
})
export class BilleterasDigitalesCineComponent {


  idUsuario!: string;


  //Arreglos que se van a llenar en base a los cines que tiene asignado el usuario
  billeterasMostradas: BilleteraCineDTO[] = [];


  //Apartado de atributos que sirven para cargar dinamicamente los atributos
  indiceActual = 0;
  cantidadPorCarga = 2;
  totalReportes = 0;
  todosCargados = false;


  constructor(
    private router: Router,
    private billeteraCineService: BilleteraCineService,
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
    this.billeterasMostradas = [];
    this.todosCargados = true;


    this.cargarCinesRegistrados();


  }


  //Metodo que permite llamar al metodo que carga dinamicamente los registros
  mostrarMasAnuncios(): void {

    if (this.todosCargados || this.billeterasMostradas.length === 0) {
      return;
    }

    this.cargarMasRegistros();
  }

  //Metodo que permite cargar los anuncios registrados
  cargarCinesRegistrados() {

    this.billeteraCineService.cantidadRegistros(this.idUsuario).subscribe({
      next: (cantidadDTO: CantidadRegistrosDTO) => {
        this.totalReportes = cantidadDTO.cantidad;
        this.indiceActual = 0;
        this.billeterasMostradas = [];
        this.todosCargados = false;

        this.cargarMasRegistros();
      },
      error: (error: any) => {

        console.log(error);

      }
    });

  }


  //Carga dinamicamente la cantidad establecida de billeteras de cine para no saturar la web
  cargarMasRegistros(): void {

    this.billeteraCineService.listadoBilleteras(this.cantidadPorCarga, this.indiceActual, this.idUsuario).subscribe({
      next: (response: BilleteraCineDTO[]) => {

        if (!response || response.length === 0) {
          this.todosCargados = true;
          return;
        }

        this.billeterasMostradas.push(...response);
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
