import { Component } from '@angular/core';
import { CineDTO } from '../../../models/cines/cine-dto';
import { ActivatedRoute, Router } from '@angular/router';
import { Popup } from '../../../shared/popup/popup';
import { CommonModule, NgIf } from '@angular/common';
import { CinesAsociadosService } from '../../../services/cine-service/cines-asociados.sercive';
import { PagoOcultacionAnunciosDTO } from '../../../models/admins-cine/pago-anuncio-dto';
import { FullscreenModalComponent } from "../../../shared/fullscreen-modal/fullscreen-modal.component";
import { SharedPopupComponent } from "../../pop-ups/shared-popup.component/shared-popup.component";
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';

@Component({
  selector: 'app-pagar-ocultacion-anuncios.component',
  imports: [NgIf, CommonModule, FullscreenModalComponent, SharedPopupComponent, ReactiveFormsModule],
  templateUrl: './pagar-ocultacion-anuncios.component.html',
  styleUrl: './pagar-ocultacion-anuncios.component.scss',
  providers: [Popup],
})
export class PagarOcultacionAnunciosComponent {

  codigoCine!: string;
  cine!: CineDTO;

  pagoForm!: FormGroup;

  //Atributos del modal 
  mostrarModal = false;
  mensajeModal = '';
  urlRedireccion = '/menu-admin-sistema/cines/asociados';
  tipoModal: 'exito' | 'error' | 'info' = 'info';

  //Atributos para mostrar el popup cuando haya un error
  mostrarPopup: boolean = false;
  mensajePopup: string = '';
  tipoPopup: 'error' | 'exito' | 'info' = 'info';
  popupKey = 0;

  constructor(
    private route: ActivatedRoute,
    private formBuilder: FormBuilder,
    private cinesAsociadosService: CinesAsociadosService,
    private router: Router,
    private popUp: Popup
  ) { }

  ngOnInit(): void {

    this.codigoCine = this.route.snapshot.params['codigo'];


    // Form reactivo
    this.pagoForm = this.formBuilder.group({
      fechaPago: ['', Validators.required]
    });

    this.cinesAsociadosService.cinePorCodigo(this.codigoCine).subscribe({
      next: (response: CineDTO) => {
        this.cine = response;
      },
      error: (err: any) => {
        this.mostrarError(err);
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

  //Metodo utilizado para abrir el modal
  abrirModal(mensaje: string, tipo: 'exito' | 'error' | 'info' = 'info') {
    this.mensajeModal = mensaje;
    this.tipoModal = tipo;
    this.mostrarModal = true;
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


  //Metodo que ejecuta la transaccion par poder realizar el pago
  confirmarPago(): void {
    if (this.pagoForm.invalid) {
      this.pagoForm.markAllAsTouched();
      return;
    }

    const fechaSeleccionada = this.pagoForm.get('fechaPago')?.value;
    const fechaISO = new Date(fechaSeleccionada).toISOString().split('T')[0];

    const pagoOcultacion: PagoOcultacionAnunciosDTO = {
      monto: this.cine.montoOcultacion,
      codigoCine: this.cine.codigo,
      fechaPago: new Date(fechaISO)
    };

    this.cinesAsociadosService.pagarOcultacionCine(pagoOcultacion).subscribe({
      next: () => {
        this.abrirModal(
          `Pago realizado exitosamente por Q${this.cine.montoOcultacion.toFixed(2)}.`,
          'exito'
        );
      },
      error: (error: any) => this.mostrarError(error)
    });
  }

  regresar(): void {
    this.router.navigate(['/menu-admin-cine/cines/asociados']);
  }

}
