import { Component, OnInit } from '@angular/core';
import { CinesAsociadosCardsComponent } from "../cines-asociados-cards/cines-asociados-cards.component";
import { Cine } from '../../../models/cines/cine';

@Component({
  selector: 'app-cines-asociados',
  imports: [CinesAsociadosCardsComponent],
  templateUrl: './cines-asociados.component.html',
  styleUrl: './cines-asociados.component.scss'
})
export class CinesAsociadosComponent implements OnInit {


  cines: Cine[] = [];
  cinesMostrados: Cine[] = [];

  //Apartado de atributos que sirven para cargar dinamicamente los atributos
  indiceActual = 0;
  cantidadPorCarga = 2;
  totalReportes = 0;
  todosCargados = false;

  //Metodo encargado de cargar los cines asociados al sistema 
  ngOnInit(): void {
    throw new Error('Method not implemented.');
  }


  //Metodo que permite ir cargando dinamicamente los cines
  cargarMasCines(): void {


  }


}
