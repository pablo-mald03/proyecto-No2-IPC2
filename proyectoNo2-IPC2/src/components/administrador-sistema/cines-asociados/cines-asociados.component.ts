import { Component, OnInit } from '@angular/core';
import { CinesAsociadosCardsComponent } from "../cines-asociados-cards/cines-asociados-cards.component";
import { Cine } from '../../../models/cines/cine';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { CineService } from '../../../services/cine-service/cine.service';
import { Popup } from '../../../shared/popup/popup';
import { CantidadRegistrosDTO } from '../../../models/usuarios/cantidad-registros-dto';
import { SharedPopupComponent } from '../../pop-ups/shared-popup.component/shared-popup.component';
import { CommonModule, NgIf } from '@angular/common';
import { CineDTO } from '../../../models/cines/cine-dto';

@Component({
  selector: 'app-cines-asociados',
  imports: [CinesAsociadosCardsComponent, CommonModule, RouterLink, RouterLinkActive, SharedPopupComponent, NgIf],
  templateUrl: './cines-asociados.component.html',
  styleUrl: './cines-asociados.component.scss',
  providers: [Popup],
})
export class CinesAsociadosComponent implements OnInit {


  cinesMostrados: CineDTO[] = [];

  //Apartado de atributos que sirven para cargar dinamicamente los atributos
  indiceActual = 0;
  cantidadPorCarga = 2;
  totalReportes = 0;
  todosCargados = false;

  //Atributos para mostrar el popup cuando haya un error
  mostrarPopup: boolean = false;
  mensajePopup: string = '';
  tipoPopup: 'error' | 'exito' | 'info' = 'info';
  popupKey = 0;


  constructor(
    private cineService: CineService,
    private popUp: Popup,
  ) { }

  //Metodo encargado de cargar los cines asociados al sistema 
  ngOnInit(): void {

    this.indiceActual = 0;
    this.cinesMostrados = [];
    this.todosCargados = true;


    this.cineService.cantidadRegistros().subscribe({
      next: (cantidadDTO: CantidadRegistrosDTO) => {

        this.totalReportes = cantidadDTO.cantidad;
        this.indiceActual = 0;
        this.cinesMostrados = [];
        this.todosCargados = false;

        this.cargarMasCines();
      },
      error: (error: any) => {

        this.mostrarError(error);

      }
    });


    this.popUp.popup$.subscribe(data => {
      this.mensajePopup = data.mensaje;
      this.tipoPopup = data.tipo;

      this.mostrarPopup = false;

      setTimeout(() => {
        this.popupKey++;
        this.mostrarPopup = true;
      }, 10);

      if (data.duracion) {
        setTimeout(() => {
          this.mostrarPopup = false;
        }, data.duracion);
      }
    });

  }

  //Metodo util para mostrar errors
  mostrarError(error: any): void {

    let mensaje = 'Ocurrió un error';

    if (error.error && error.error.mensaje) {
      mensaje = error.error.mensaje;
    } else if (error.message) {
      mensaje = error.message;
    }

    this.popUp.mostrarPopup({ mensaje, tipo: 'error' });
  }


  //Metodo que permite ir cargando dinamicamente los cines
  cargarMasCines(): void {

    if (this.todosCargados) return;

    this.cineService.listadoRegistros(this.cantidadPorCarga, this.indiceActual).subscribe({
      next: (response: CineDTO[]) => {
        this.ampliarResultados(response);

      },
      error: (error: any) => {

        this.mostrarError(error);

      }
    });


  }

  //Metodo encargado de ir ampliando dinamicamente los cines registrados o asociados en el sistema
  ampliarResultados(response: CineDTO[]): void {

    if (!response || response.length === 0) {
      this.todosCargados = true;
      return;
    }

    this.cinesMostrados.push(...response);
    this.indiceActual += response.length;

    if (this.indiceActual >= this.totalReportes) {
      this.todosCargados = true;
    }

  }



}
