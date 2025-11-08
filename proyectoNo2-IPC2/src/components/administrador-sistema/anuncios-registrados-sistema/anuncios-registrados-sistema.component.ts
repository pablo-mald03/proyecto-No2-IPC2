import { Component, OnInit } from '@angular/core';
import { AnuncioRegistradoDTO } from '../../../models/anuncios/anuncio-registrado-dto';
import { AnunciosRegistradosSistemaCardsComponent } from "../anuncios-registrados-sistema-cards/anuncios-registrados-sistema-cards.component";
import { Popup } from '../../../shared/popup/popup';
import { SharedPopupComponent } from '../../pop-ups/shared-popup.component/shared-popup.component';
import { CommonModule, NgIf } from '@angular/common';
import { AnunciosRegistradosService } from '../../../services/anuncios-service/anuncios-registrados.service';
import { CantidadReportesDTO } from '../../../models/reportes/cantidad-reportes-dto';
import { ConfirmModalComponent } from "../../../shared/confirm-modal/confirm-modal.component";

@Component({
  selector: 'app-anuncios-registrados-sistema',
  imports: [AnunciosRegistradosSistemaCardsComponent, SharedPopupComponent, NgIf, CommonModule, ConfirmModalComponent],
  templateUrl: './anuncios-registrados-sistema.component.html',
  styleUrl: './anuncios-registrados-sistema.component.scss',
  providers: [Popup],
})
export class AnunciosRegistradosSistemaComponent implements OnInit {

  //Arreglos que se van a llenar en base a los anuncios que posea el usuario
  anunciosMostrados: AnuncioRegistradoDTO[] = [];

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
    private popUp: Popup,
    private anunciosRegistradosService: AnunciosRegistradosService,
  ) {

  }

  //Metodo que permite tomar el parametro enviado
  ngOnInit(): void {


    this.indiceActual = 0;
    this.anunciosMostrados = [];
    this.todosCargados = true;

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

    this.cargarAnunciosRegistrados();


  }

  cargarAnunciosRegistrados() {

    this.anunciosRegistradosService.cantidadRegistros().subscribe({
      next: (cantidadDTO: CantidadReportesDTO) => {
        this.totalReportes = cantidadDTO.cantidad;
        this.indiceActual = 0;
        this.anunciosMostrados = [];
        this.todosCargados = false;

        this.cargarMasRegistros();
      },
      error: (error: any) => {

        this.mostrarError(error);

      }
    });

  }

  //Metodo implementado para mostrar los mensajes de errores
  mostrarError(errorEncontrado: any) {
    let mensaje = 'Ocurrió un error';

    if (errorEncontrado.error && errorEncontrado.error.mensaje) {
      mensaje = errorEncontrado.error.mensaje;
    } else if (errorEncontrado.message) {
      mensaje = errorEncontrado.message;
    }

    this.popUp.mostrarPopup({ mensaje, tipo: 'error' });

  }

  //Metodo que permite llamar al metodo que carga dinamicamente los registros
  mostrarMasAnuncios(): void {

    if (this.todosCargados || this.anunciosMostrados.length === 0) {
      return;
    }

    this.cargarMasRegistros();
  }

  //Carga dinamicamente la cantidad establecida de anuncios para no saturar la web
  cargarMasRegistros(): void {



    this.anunciosRegistradosService.listadoRegistros(this.cantidadPorCarga, this.indiceActual).subscribe({
      next: (response: AnuncioRegistradoDTO[]) => {

        if (!response || response.length === 0) {
          this.todosCargados = true;
          return;
        }

        this.anunciosMostrados.push(...response);
        this.indiceActual += response.length;

        if (this.indiceActual >= this.totalReportes) {
          this.todosCargados = true;
        }


      },
      error: (error: any) => {

        this.mostrarError(error);

      }
    });
  }

}
