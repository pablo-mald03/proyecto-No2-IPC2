import { Component, OnInit } from '@angular/core';
import { ConfiguracionAnuncioDTO } from '../../../models/anuncios/configuracion-anuncio-dto';
import { ConfiguracionAnunciosService } from '../../../services/anuncios-service/configuracion-anuncios.service';
import { Popup } from '../../../shared/popup/popup';
import { SharedPopupComponent } from '../../pop-ups/shared-popup.component/shared-popup.component';
import { CommonModule, NgIf } from '@angular/common';
import { ConfigurarPreciosCardsComponent } from "../configurar-precios-cards/configurar-precios-cards.component";
import { Router } from '@angular/router';

@Component({
  selector: 'app-configurar-precios',
  imports: [SharedPopupComponent, NgIf, CommonModule, ConfigurarPreciosCardsComponent],
  templateUrl: './configurar-precios.component.html',
  styleUrl: './configurar-precios.component.scss',
  providers: [Popup],
})
export class ConfigurarPreciosComponent implements OnInit {

  configuracionesAnuncios: ConfiguracionAnuncioDTO[] = [];

  //Atributos del popup
  mostrarPopup = false;
  mensajePopup = '';
  tipoPopup: 'error' | 'exito' | 'info' = 'info';
  popupKey = 0;

  constructor(

    private configuracionAnunciosService: ConfiguracionAnunciosService,
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


    this.configuracionAnunciosService.listadoConfiguraciones().subscribe({
      next: (cantidadDTO: ConfiguracionAnuncioDTO[]) => {

        this.configuracionesAnuncios = cantidadDTO;


      },
      error: (error: any) => {

        this.mostrarError(error);

      }
    });


  }

  //Metodo que permite regresar
  regresar(): void{
    this.router.navigateByUrl('/menu-admin-sistema/costos');
    
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
}
