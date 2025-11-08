import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { AnunciosCompradosAnuncianteCardsComponent } from "../anuncios-comprados-anunciante-cards/anuncios-comprados-anunciante-cards.component";
import { Anuncio } from '../../../models/anuncios/anuncio';
import { Popup } from '../../../shared/popup/popup';
import { AnunciosRegistradosClienteService } from '../../../services/anuncios-service/anuncios-registrados-cliente.service';
import { CantidadRegistrosDTO } from '../../../models/usuarios/cantidad-registros-dto';
import { AnuncioRegistradoDTO } from '../../../models/anuncios/anuncio-registrado-dto';
import { SharedPopupComponent } from "../../pop-ups/shared-popup.component/shared-popup.component";
import { ConfirmModalComponent } from "../../../shared/confirm-modal/confirm-modal.component";
import { NgIf } from '@angular/common';

@Component({
  selector: 'app-anuncios-comprados',
  imports: [AnunciosCompradosAnuncianteCardsComponent, SharedPopupComponent, ConfirmModalComponent, NgIf],
  templateUrl: './anuncios-comprados.component.html',
  styleUrl: './anuncios-comprados.component.scss',
  providers: [Popup],
})
export class AnunciosCompradosComponent implements OnInit {

  idUsuario!: string;

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
    private router: ActivatedRoute,
    private popUp: Popup,
    private anunciosRegistradosService: AnunciosRegistradosClienteService,

  ) {

  }

  //Metodo que permite tomar el parametro enviado
  ngOnInit(): void {

    this.idUsuario = this.router.snapshot.params['id'];


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

  //Metodo que permite llamar al metodo que carga dinamicamente los registros
  mostrarMasAnuncios(): void {

    if (this.todosCargados || this.anunciosMostrados.length === 0) {
      return;
    }

    this.cargarMasRegistros();
  }


  //Metodo que permite cargar los anuncios registrados
  cargarAnunciosRegistrados() {

    this.anunciosRegistradosService.cantidadRegistros(this.idUsuario).subscribe({
      next: (cantidadDTO: CantidadRegistrosDTO) => {
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

  //Carga dinamicamente la cantidad establecida de anuncios para no saturar la web
  cargarMasRegistros(): void {

    this.anunciosRegistradosService.listadoRegistros(this.cantidadPorCarga, this.indiceActual, this.idUsuario).subscribe({
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
