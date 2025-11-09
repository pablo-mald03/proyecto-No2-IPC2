import { Component } from '@angular/core';
import { BilleteraCineDTO } from '../../../models/admins-cine/billetera-cine-dto';
import { Router } from '@angular/router';

@Component({
  selector: 'app-billeteras-digitales-cine',
  imports: [],
  templateUrl: './billeteras-digitales-cine.component.html',
  styleUrl: './billeteras-digitales-cine.component.scss'
})
export class BilleterasDigitalesCineComponent {


   idUsuario!: string;


  //Arreglos que se van a llenar en base a los cines que tiene asignado el usuario
  cinesMostrados: BilleteraCineDTO[] = [];


  //Apartado de atributos que sirven para cargar dinamicamente los atributos
  indiceActual = 0;
  cantidadPorCarga = 2;
  totalReportes = 0;
  todosCargados = false;


  constructor(
    private router: Router,
    ///private cinesAsociadosService: CinesAsociadosService,
  ) {

  }


}
