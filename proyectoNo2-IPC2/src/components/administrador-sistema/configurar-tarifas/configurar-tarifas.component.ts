import { Component, OnInit } from '@angular/core';
import { SharedPopupComponent } from "../../pop-ups/shared-popup.component/shared-popup.component";
import { Popup } from '../../../shared/popup/popup';
import { VigenciaAnunciosService } from '../../../services/anuncios-service/vigencia-anuncios.service';
import { Router } from '@angular/router';
import { VigenciaAnuncio } from '../../../models/anuncios/vigencia-anuncio';
import { VigenciaAnunciosCardsComponent } from "../../pagina-anunciante/vigencia-anuncios-cards/vigencia-anuncios-cards.component";
import { CommonModule, NgIf } from '@angular/common';
import { ConfigurarVigenciasCardsComponent } from "../configurar-vigencias-cards/configurar-vigencias-cards.component";

@Component({
  selector: 'app-configurar-tarifas',
  imports: [SharedPopupComponent, CommonModule, NgIf, ConfigurarVigenciasCardsComponent],
  templateUrl: './configurar-tarifas.component.html',
  styleUrl: './configurar-tarifas.component.scss',
  providers: [Popup],
})
export class ConfigurarTarifasComponent implements OnInit {



  vigenciasAnuncios: VigenciaAnuncio[] = [];

  //Atributos del popup
  mostrarPopup = false;
  mensajePopup = '';
  tipoPopup: 'error' | 'exito' | 'info' = 'info';
  popupKey = 0;

  constructor(

    private vigenciaAnuncioService: VigenciaAnunciosService,
    private popUp: Popup,
    private router: Router,

  ) { }
  ngOnInit(): void {

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


    this.vigenciaAnuncioService.listadoVigencias().subscribe({
      next: (cantidadDTO: VigenciaAnuncio[]) => {

        this.vigenciasAnuncios = cantidadDTO;


      },
      error: (error: any) => {

        this.mostrarError(error);

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

  //Metodo que permite regresar 
  regresar(): void {

    this.router.navigateByUrl('/menu-admin-sistema/costos');
  }

}
